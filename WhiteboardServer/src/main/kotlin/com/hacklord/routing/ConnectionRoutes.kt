package com.hacklord.routing

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import com.hacklord.components.OnlineUserState
import com.hacklord.components.Whiteboard
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
import com.hacklord.managers.UserManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import java.util.*

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
                        println(frame.readText())
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

        try {
            incoming.consumeAsFlow().collect {
                if (it is Frame.Text) {
                    when (onlineUser.state) {
                        is OnlineUserState.InLobby -> {
                            val response: LobbyResponse

                            val json = Json {
                                serializersModule = lobbyRequestModule
                                classDiscriminator = "code"
                            }

                            println(it.readText())

                            when (val request = json.decodeFromString<LobbyRequest>(it.readText())) {
                                is LobbyRequest.GetWhiteboards -> {
                                    val dbBoards = whiteboardDataSource.getAllWhiteboardsOfUser(onlineUser.user.id)

                                    val boards = mutableListOf<Whiteboard>()

                                    dbBoards.forEach { board ->
                                        val onlineBoard = onlineBoardsManager.onlineBoards[board.id]

                                        boards.add(
                                            onlineBoard?.getBoardInfo() ?: board
                                        )
                                    }

                                    response = LobbyResponse.BoardList(boards = dbBoards)
                                }

                                is LobbyRequest.EnterWhiteboard -> {
                                    response = if (onlineBoardsManager.connectUser(onlineUser, request.boardID)) {
                                        onlineUser = UserManager.changeState(
                                            onlineUser,
                                            OnlineUserState.InWhiteboard(request.boardID)
                                        )

                                        LobbyResponse.EnterBoard(
                                            boardInfo = onlineBoardsManager.onlineBoards[request.boardID]!!.getBoardInfo()
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
                                        boardInfo = onlineBoardsManager.onlineBoards[boardID]!!.getBoardInfo()
                                    )
                                }

                                is LobbyRequest.DeleteWhiteboard -> {
                                    if (onlineBoardsManager.onlineBoards.keys.contains(request.boardID))
                                        onlineBoardsManager.closeWhiteboard(request.boardID)

                                    whiteboardDataSource.deleteWhiteboard(ObjectId(request.boardID))

                                    response = LobbyResponse.DeleteBoard
                                }
                            }

                            println(Json.encodeToString(response))

                            send(
                                Frame.Text(
                                    Json.encodeToString(response)
                                )
                            )
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

                            println(request)

                            when (request) {
                                is WhiteboardRequest.DrawLine -> {
                                    manager.drawLine(request.line)
                                    onlineBoardsManager.saveBoard(manager.id)

                                    manager.broadcast(
                                        onlineUser.user.id,
                                        WhiteboardBroadcast.DrawBroadcast(
                                            line = request.line
                                        )
                                    )

                                    response = WhiteboardResponse.DrawLine
                                }

                                is WhiteboardRequest.EraseLine -> {
                                    val success = manager.eraseLine(
                                        UUID.fromString(request.lineId)
                                    )
                                    onlineBoardsManager.saveBoard(manager.id)

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
                                    if (!onlineBoardsManager.disconnectUser(
                                            onlineUser.user.id,
                                            userState.boardId
                                        )
                                    ) {
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

                                is WhiteboardRequest.SearchUsers -> {
                                    val users = userDataSource.getAllUsers(request.name)

                                    response = WhiteboardResponse.GetAllUsers(
                                        users
                                    )
                                }

                                is WhiteboardRequest.AddUser -> {
                                    val id = userDataSource.getUserById(
                                        request.id
                                    )?.id

                                    if (id == null)
                                        response = WhiteboardResponse.Error(
                                            message = "Username not found."
                                        )
                                    else {
                                        manager.addUser(id)
                                        onlineBoardsManager.saveBoard(manager.id)
                                    }
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

                            println(Json.encodeToString(response))

                            if (response != null)
                                send(
                                    Frame.Text(
                                        Json.encodeToString(response)
                                    )
                                )
                        }
                    }
                } else {
                    send("Unsupported frame")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("An error occurred: ${e.message}")
        } finally {
            when (val state = onlineUser.state) {
                is OnlineUserState.InWhiteboard -> {
                    if (!onlineBoardsManager.disconnectUser(onlineUser.user.id, state.boardId)) {
                        println("An error occurred while disconnecting user: ${onlineUser.user}")
                    }
                }
                else -> {}
            }

            if (!UserManager.disconnectUser(userId)) {
                println("An error occurred while disconnecting user: ${onlineUser.user}")
            }

            println("Disconnected user: ${onlineUser.user}")
        }
    }
}

fun getWhiteboardInfo(boardID: String) {

}