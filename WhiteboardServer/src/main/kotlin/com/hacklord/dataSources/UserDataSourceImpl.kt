package com.hacklord.dataSources

import com.hacklord.components.User
import com.hacklord.interfaces.UserDataSource
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDataSourceImpl(
    db: CoroutineDatabase
) : UserDataSource {
    private val users = db.getCollection<User>(collectionName = "users")

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::name eq username)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOne(User::id eq id)
    }

    override suspend fun getAllUsers(): List<User> {
        return users.find().toList()
    }

    override suspend fun insertUser(user: User): ObjectId? {
        return if (users.insertOne(user).wasAcknowledged())
            return ObjectId(user.id) else null
    }

    override suspend fun updateUser(user: User): Boolean {
        return users.updateOneById(user.id, user).wasAcknowledged()
    }

}