package com.hacklord.managers

import com.hacklord.components.OnlineUser
import com.hacklord.components.Whiteboard
import com.hacklord.dataSources.WhiteboardDataSourceImpl
import com.hacklord.managers.UserManager.onlineUsers
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

class OnlineBoardsManager(
    database: CoroutineDatabase
) {
    val onlineBoards: HashMap<String, WhiteboardManager> = hashMapOf()
    private val boardsDataSource = WhiteboardDataSourceImpl(database)

    private suspend fun openWhiteboard(boardId: String): WhiteboardManager {
        val board = boardsDataSource.getWhiteboardById(ObjectId(boardId))

        board ?: throw Exception("Invalid whiteboard ID")

        val manager = WhiteboardManager(board)

        onlineBoards[manager.id] = manager

        return manager
    }

    suspend fun createWhiteboard(name: String, ownerID: String): String {
        val newBoard = Whiteboard(
            name = name,
            creator = ownerID,
        )

        val id = boardsDataSource.insertWhiteboard(newBoard)?.toString()

        id ?: throw Exception("Internal error in database.")

        openWhiteboard(id)

        return id
    }

    suspend fun closeWhiteboard(boardID: String) {
        val manager = onlineBoards[boardID]

        manager ?: throw Exception("Invalid whiteboard ID")

        val board = manager.closeBoard()

        boardsDataSource.updateWhiteboard(board)
    }

    suspend fun connectUser(user: OnlineUser, boardID: String): Boolean {
        val board = onlineBoards[boardID] ?: openWhiteboard(boardID)

        if (board.getBoardInfo().creator == user.user.id || board.whitelist.contains(user.user.id)) {
            println(onlineUsers)
            board.connectUser(onlineUsers[user.user.id]!!)

            return true
        }

        return false
    }

    suspend fun disconnectUser(userId: String, boardID: String): Boolean {
        val manager = onlineBoards[boardID] ?: return false

        if (!manager.disconnectUser(userId)) return false

        if (manager.connectedUsers.isEmpty()) {
            manager.closeBoard()
            val saved = saveBoard(boardID)

            onlineBoards.remove(boardID)

            return saved
        }

        return true
    }

    suspend fun saveBoard(boardID: String): Boolean {
        val manager = onlineBoards[boardID] ?: return false

        return boardsDataSource.updateWhiteboard(manager.getBoardInfo())
    }
}