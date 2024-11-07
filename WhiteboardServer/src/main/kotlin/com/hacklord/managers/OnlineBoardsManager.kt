package com.hacklord.managers

import com.hacklord.components.OnlineUser
import com.hacklord.components.User
import com.hacklord.components.Whiteboard
import com.hacklord.dataSources.WhiteboardDataSourceImpl
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class OnlineBoardsManager {
    private val onlineBoards: HashMap<Long, WhiteboardManager> = hashMapOf()
    private val boardsDataSource = WhiteboardDataSourceImpl()

    private var currBoardId: Long = 0

    companion object {
        val onlineUsers = ConcurrentHashMap<Long, OnlineUser>()
    }

    fun openWhiteboard(boardId: Long) {
        val board = boardsDataSource.getWhiteboardById(boardId)

        board ?: throw Exception("Invalid whiteboard ID")

        val manager = WhiteboardManager(
            board
        )

        onlineBoards[manager.info.id] = manager
    }

    fun createWhiteboard(name: String, owner: User) {
        boardsDataSource.createBoard(
            Whiteboard(
                ++currBoardId,
                name,
                owner,
            )
        )

        openWhiteboard(currBoardId)
    }

    fun closeWhiteboard(boardID: Long) {
        val manager = onlineBoards[boardID]

        manager ?: throw Exception("Invalid whiteboard ID")

        val board = manager.closeBoard()

        boardsDataSource.updateWhiteboard(board)
    }

    fun connectUser(user: User, boardID: Long, session: WebSocketSession): Boolean {
        val board = onlineBoards[boardID]

        board ?: return false

        if (board.whitelist.contains(user.id)) {
            onlineUsers[user.id] = OnlineUser(
                user = user,
                currBoard = boardID,
                session = session
            )

            board.connectUser(onlineUsers[user.id]!!)

            return true
        }

        return false
    }
}