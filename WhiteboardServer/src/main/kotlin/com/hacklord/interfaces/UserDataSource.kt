package com.hacklord.interfaces

import com.hacklord.components.UserEntity
import org.bson.types.ObjectId

interface UserDataSource {
    suspend fun getUserByUsername(username: String): UserEntity?
    suspend fun getUserById(id: String): UserEntity?
    suspend fun getAllUsers(name: String): List<UserEntity>
    suspend fun insertUser(user: UserEntity): ObjectId?
    suspend fun updateUser(user: UserEntity): Boolean
}