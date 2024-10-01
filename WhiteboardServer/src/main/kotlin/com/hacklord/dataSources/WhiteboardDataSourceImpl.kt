package com.hacklord.dataSources

import com.hacklord.components.Whiteboard
import com.hacklord.interfaces.WhiteboardDataSource

class WhiteboardDataSourceImpl : WhiteboardDataSource {
    private val boards = HashMap<Long, Whiteboard>()

    override fun createBoard(boardData: Whiteboard) {
        boards[boardData.id] = boardData
    }

    override fun deleteWhiteboard(id: Long) {
        boards.remove(id)
    }

    override fun editWhiteboard(boardData: Whiteboard): Boolean {
        if (!boards.containsKey(boardData.id)) return false

        boards[boardData.id] = boardData

        return true
    }

    override fun getWhiteboardById(id: Long): Whiteboard? {
        return boards[id]
    }
}