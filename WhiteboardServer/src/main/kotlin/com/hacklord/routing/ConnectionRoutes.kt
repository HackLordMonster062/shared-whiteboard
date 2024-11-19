package com.hacklord.routing

import com.hacklord.components.OnlineUserState
import com.hacklord.interfaces.UserDataSource
import com.hacklord.managers.UserManager
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId

fun Route.connection(
    userDataSource: UserDataSource,
    userManager: UserManager
) {
    authenticate {
        webSocket("/connect") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: run {
                call.respond(HttpStatusCode.BadRequest, "User ID not given.")
                return@webSocket
            }
            val user = userDataSource.getUserById(ObjectId(userId)) ?: kotlin.run {
                call.respond(HttpStatusCode.Conflict, "Invalid user ID.")
                return@webSocket
            }

            var onlineUser = userManager.connectUser(user, this)

            incoming.consumeEach {
                if (it is Frame.Text) {
                    when (onlineUser.state) {
                        is OnlineUserState.InLobby -> {
                            val request = Json.decodeFromString<LobbyRequest>(it.readText())

                            when (request) {
                                is LobbyRequest.GetWhiteboards -> {

                                }
                                is LobbyRequest.EnterWhiteboard -> {
                                    onlineUser = userManager.changeState(
                                        onlineUser,
                                        OnlineUserState.InWhiteboard(request.boardID)
                                    )
                                }
                            }
                        }
                        is OnlineUserState.InWhiteboard -> {
                            val request = Json.decodeFromString<WhiteboardRequest>(it.readText())

                            val response = when (request) {
                                is WhiteboardRequest.DrawLine -> {
                                    Response.DrawBroadcast(
                                        line = request.line
                                    )
                                }

                                is WhiteboardRequest.EraseLine -> {
                                    Response.EraseBroadcast(
                                        lineID = request.lineId
                                    )
                                }
                            }

                            userManager.onlineUsers.values.forEach { user ->
                                if (user.session != this) {
                                    user.session.send(Frame.Text(
                                        Json.encodeToString(response)
                                    ))
                                }
                            }
                        }
                    }
                } else {
                    send("Unsupported frame")
                }
            }
        }
    }
}