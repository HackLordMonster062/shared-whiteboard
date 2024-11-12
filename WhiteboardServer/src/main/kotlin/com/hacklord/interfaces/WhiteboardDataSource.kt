package com.hacklord.interfaces

import com.hacklord.components.Whiteboard
import org.bson.types.ObjectId

interface WhiteboardDataSource {
    suspend fun getWhiteboardByName(name: String): Whiteboard?
    suspend fun getWhiteboardById(id: ObjectId): Whiteboard?
    suspend fun getAllWhiteboards(): List<Whiteboard>
    suspend fun insertWhiteboard(board: Whiteboard): ObjectId?
    suspend fun updateWhiteboard(board: Whiteboard): Boolean
}