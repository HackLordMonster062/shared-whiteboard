package com.hacklord.routing

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import com.hacklord.components.OnlineUserState
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
import com.hacklord.managers.UserManager
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
    onlineBoardsManager: OnlineBoardsManager,
    jwtVerifier: JWTVerifier
) {
    webSocket("/connect") {
        var connectRequest: GeneralRequest.Connect? = null

        while (connectRequest == null) {
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        connectRequest = Json.decodeFromString<GeneralRequest.Connect>(frame.readText())
                        break
                    }
                    else -> continue
                }
            }
        }

        val decodedJWT: DecodedJWT = jwtVerifier.verify(connectRequest.token)

        val userId = decodedJWT.getClaim("userId").asString() ?: run {
            send(
                Frame.Text(
                    "No user ID given"
                )
            )
            return@webSocket
        }

        val user = userDataSource.getUserById(userId) ?: kotlin.run {
            send(
                Frame.Text(
                    "User not found"
                )
            )
            return@webSocket
        }

        var onlineUser = UserManager.connectUser(user, this)

        incoming.consumeEach {
            if (it is Frame.Text) {
                when (onlineUser.state) {
                    is OnlineUserState.InLobby -> {
                        val response: LobbyResponse

                        val json = Json {
                            serializersModule = lobbyRequestModule
                            classDiscriminator = "code"
                        }

                        when (val request = json.decodeFromString<LobbyRequest>(it.readText())) {
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
                                    ownerID = onlineUser.user.id
                                )

                                onlineUser = UserManager.changeState(
                                    onlineUser,
                                    OnlineUserState.InWhiteboard(boardID)
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

                                response = LobbyResponse.DeleteBoard
                            }
                        }

                        send(Frame.Text(
                            Json.encodeToString(response)
                        ))
                    }
                    is OnlineUserState.InWhiteboard -> {
                        val json = Json {
                            serializersModule = whiteboardRequestModule
                            classDiscriminator = "code"
                        }

                        val request = json.decodeFromString<WhiteboardRequest>(it.readText())

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

                                    response = WhiteboardResponse.EraseLine
                                } else {
                                    response = WhiteboardResponse.Error(
                                        message = "Invalid line ID"
                                    )
                                }
                            }

                            is WhiteboardRequest.ExitBoard -> {
                                if (!onlineBoardsManager.disconnectUser(onlineUser.user.id, userState.boardId)) {
                                    response = WhiteboardResponse.Error(
                                        message = "An error occurred"
                                    )
                                } else {
                                    response = WhiteboardResponse.ExitBoard

                                    onlineUser = UserManager.changeState(
                                        onlineUser,
                                        OnlineUserState.InLobby
                                    )
                                }
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