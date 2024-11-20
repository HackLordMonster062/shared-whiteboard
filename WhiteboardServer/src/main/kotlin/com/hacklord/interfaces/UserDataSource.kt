package com.hacklord.interfaces

import com.hacklord.components.User
import org.bson.types.ObjectId

interface UserDataSource {
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserById(id: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun insertUser(user: User): ObjectId?
    suspend fun updateUser(user: User): Boolean
}