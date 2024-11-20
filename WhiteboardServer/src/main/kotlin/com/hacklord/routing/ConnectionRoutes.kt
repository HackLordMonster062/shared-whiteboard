package com.hacklord.routing

import com.hacklord.components.OnlineUserState
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
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
    whiteboardDataSource: WhiteboardDataSource,
    onlineBoardsManager: OnlineBoardsManager
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

            var onlineUser = UserManager.connectUser(user, this)

            incoming.consumeEach {
                if (it is Frame.Text) {
                    when (onlineUser.state) {
                        is OnlineUserState.InLobby -> {
                            val response: LobbyResponse

                            when (val request = Json.decodeFromString<LobbyRequest>(it.readText())) {
                                is LobbyRequest.GetWhiteboards -> {
                                    val boards = whiteboardDataSource.getAllWhiteboards()

                                    response = LobbyResponse.BoardList(boards = boards)
                                }
                                is LobbyRequest.EnterWhiteboard -> {
                                    onlineUser = UserManager.changeState(
                                        onlineUser,
                                        OnlineUserState.InWhiteboard(request.boardID)
                                    )

                                    response = if (onlineBoardsManager.connectUser(onlineUser, request.boardID)) {
                                        LobbyResponse.EnterBoard(
                                            boardInfo = onlineBoardsManager.onlineBoards[request.boardID]!!.info
                                        )
                                    } else {
                                        LobbyResponse.Error(
                                            message = "Cannot enter board."
                                        )
                                    }
                                }
                                is LobbyRequest.CreateWhiteboard -> {
                                    val boardID = onlineBoardsManager.createWhiteboard(
                                        name = request.name,
                                        owner = onlineUser.user
                                    )

                                    onlineBoardsManager.connectUser(
                                        user = onlineUser,
                                        boardID = boardID
                                    )

                                    response = LobbyResponse.EnterBoard(
                                        boardInfo = onlineBoardsManager.onlineBoards[boardID]!!.info
                                    )
                                }
                                is LobbyRequest.DeleteWhiteboard -> {
                                    if (onlineBoardsManager.onlineBoards.keys.contains(request.boardID))
                                        onlineBoardsManager.closeWhiteboard(request.boardID)

                                    whiteboardDataSource.deleteWhiteboard(ObjectId(request.boardID))

                                    response = LobbyResponse.DeleteBoard()
                                }
                            }

                            send(Frame.Text(
                                Json.encodeToString(response)
                            ))
                        }
                        is OnlineUserState.InWhiteboard -> {
                            val request = Json.decodeFromString<WhiteboardRequest>(it.readText())

                            val userState = onlineUser.state as OnlineUserState.InWhiteboard
                            val manager = onlineBoardsManager.onlineBoards[userState.boardId]!!

                            var response: WhiteboardResponse? = null

                            when (request) {
                                is WhiteboardRequest.DrawLine -> {
                                    val id = manager.drawLine(request.line)

                                    if (id == null) {
                                        response = WhiteboardResponse.Error(
                                            message = "Invalid line"
                                        )
                                    } else {
                                        manager.broadcast(
                                            onlineUser.user.id,
                                            WhiteboardBroadcast.DrawBroadcast(
                                                line = request.line
                                            )
                                        )

                                        response = WhiteboardResponse.DrawLine(
                                            lineId = id
                                        )
                                    }
                                }

                                is WhiteboardRequest.EraseLine -> {
                                    val success = manager.eraseLine(request.lineId)

                                    if (success) {
                                        manager.broadcast(
                                            onlineUser.user.id,
                                            WhiteboardBroadcast.EraseBroadcast(
                                                lineID = request.lineId
                                            )
                                        )

                                        response = WhiteboardResponse.EraseLine()
                                    } else {
                                        response = WhiteboardResponse.Error(
                                            message = "Invalid line ID"
                                        )
                                    }
                                }

                                is WhiteboardRequest.ExitBoard -> {
                                    manager.disconnectUser(onlineUser.user.id)

                                    response = WhiteboardResponse.ExitBoard()
                                }

                                is WhiteboardRequest.AddUser -> {
                                    val id = userDataSource.getUserByUsername(
                                        request.username
                                    )?.id

                                    if (id == null)
                                        response = WhiteboardResponse.Error(
                                            message = "Username not found."
                                        )
                                    else
                                        manager.addUser(id)
                                }

                                is WhiteboardRequest.RemoveUser -> {
                                    val id = userDataSource.getUserByUsername(
                                        request.username
                                    )?.id

                                    if (id == null)
                                        response = WhiteboardResponse.Error(
                                            message = "Username not found."
                                        )
                                    else
                                        manager.removeUser(id)
                                }
                            }

                            if (response != null)
                                send(Frame.Text(
                                    Json.encodeToString(response)
                                ))
                        }
                    }
                } else {
                    send("Unsupported frame")
                }
            }
        }
    }
}