package com.hacklord.interfaces

import com.hacklord.components.Whiteboard

interface WhiteboardDataSource {
    fun createBoard(boardData: Whiteboard)
    fun deleteWhiteboard(id: Long)
    fun editWhiteboard(boardData: Whiteboard): Boolean
    fun getWhiteboardById(id: Long): Whiteboard?
}