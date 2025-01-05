package com.hacklord.dataSources

import com.hacklord.components.Whiteboard
import com.hacklord.interfaces.WhiteboardDataSource
import org.bson.types.ObjectId
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

class WhiteboardDataSourceImpl(
    db: CoroutineDatabase
) : WhiteboardDataSource {
    private val boards = db.getCollection<Whiteboard>(collectionName = "whiteboards")

    override suspend fun getWhiteboardByName(name: String): Whiteboard? {
        return boards.findOne(Whiteboard::name eq name)
    }

    override suspend fun getWhiteboardById(id: ObjectId): Whiteboard? {
        return boards.findOne(Whiteboard::id eq id.toString())
    }

    override suspend fun getAllWhiteboardsOfUser(userId: String): List<Whiteboard> {
        return boards.find(
            or(Whiteboard::userWhitelist contains userId, Whiteboard::creator eq userId)
        ).toList()
    }

    override suspend fun insertWhiteboard(board: Whiteboard): ObjectId? {
        return if (boards.insertOne(board).wasAcknowledged())
            return ObjectId(board.id) else null
    }

    override suspend fun updateWhiteboard(board: Whiteboard): Boolean {
        return boards.updateOne(Whiteboard::id eq board.id, board).wasAcknowledged()
    }

    override suspend fun deleteWhiteboard(id: ObjectId): Boolean {
        return boards.deleteOneById(id).wasAcknowledged()
    }
}