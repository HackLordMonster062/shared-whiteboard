package com.hacklord.dataSources

import com.hacklord.components.User
import com.hacklord.interfaces.UserDataSource

class UserDataSourceImpl : UserDataSource {
    private val users = HashMap<Long, User>()

    override fun createUser(user: User) {
        users[user.id] = user
    }

    override fun deleteUser(id: Long) {
        users.remove(id)
    }

    override fun getUserById(id: Long): User? {
        return users[id]
    }

}