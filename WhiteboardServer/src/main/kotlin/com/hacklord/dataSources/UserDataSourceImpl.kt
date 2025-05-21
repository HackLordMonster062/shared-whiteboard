package com.hacklord.dataSources

import com.hacklord.components.UserEntity
import com.hacklord.interfaces.UserDataSource
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.regex

class UserDataSourceImpl(
    db: CoroutineDatabase
) : UserDataSource {
    private val users = db.getCollection<UserEntity>(collectionName = "users")

    override suspend fun getUserByUsername(username: String): UserEntity? {
        return users.findOne(UserEntity::name eq username)
    }

    override suspend fun getUserById(id: String): UserEntity? {
        return users.findOne(UserEntity::id eq id)
    }

    override suspend fun getAllUsers(name: String): List<UserEntity> {
        return users.find(UserEntity::name.regex(".*$name.*", "i")).toList()
    }

    override suspend fun insertUser(user: UserEntity): ObjectId? {
        return if (users.insertOne(user).wasAcknowledged())
            return ObjectId(user.id) else null
    }

    override suspend fun updateUser(user: UserEntity): Boolean {
        return users.updateOneById(user.id, user).wasAcknowledged()
    }

}