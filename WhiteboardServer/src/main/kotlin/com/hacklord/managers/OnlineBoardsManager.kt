package com.hacklord.managers

import com.hacklord.components.User
import com.hacklord.components.Whiteboard
import java.util.UUID

class OnlineBoardsManager {
    private val onlineBoards: HashMap<Long, WhiteboardManager> = hashMapOf()

    fun openWhiteboard(boardId: UUID) {
        val manager = WhiteboardManager(
            Whiteboard(
                0,
                "board",
                User("user", 0),
            )
        ) // TODO: pull from database

        onlineBoards[manager.info.id] = manager
    }
}