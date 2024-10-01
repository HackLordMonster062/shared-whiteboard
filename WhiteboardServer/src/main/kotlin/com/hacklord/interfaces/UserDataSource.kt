package com.hacklord.interfaces

import com.hacklord.components.User

interface UserDataSource {
    fun createUser(user: User)
    fun deleteUser(id: Long)
    fun getUserById(id: Long): User?
}