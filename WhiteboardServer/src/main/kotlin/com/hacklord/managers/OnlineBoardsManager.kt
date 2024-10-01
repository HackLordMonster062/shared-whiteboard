package com.hacklord.managers

import com.hacklord.components.User
import com.hacklord.components.Whiteboard
import com.hacklord.dataSources.WhiteboardDataSourceImpl

class OnlineBoardsManager {
    private val onlineBoards: HashMap<Long, WhiteboardManager> = hashMapOf()
    private val boardsDataSource = WhiteboardDataSourceImpl()

    private var currBoardId: Long = 0

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

    fun connectUser(userID: Long, boardID: Long) {
        onlineBoards[boardID]?.addUser(userID)
    }
}