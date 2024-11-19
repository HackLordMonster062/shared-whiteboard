package com.hacklord.managers

import com.hacklord.components.OnlineUser
import com.hacklord.components.OnlineUserState
import com.hacklord.components.User
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

object UserManager {
    val onlineUsers = ConcurrentHashMap<String, OnlineUser>()

    fun connectUser(user: User, session: WebSocketSession): OnlineUser {
        val onlineUser = OnlineUser(
            user,
            OnlineUserState.InLobby,
            session
        )

        onlineUsers[user.id] = onlineUser

        return onlineUser
    }

    fun changeState(user: OnlineUser, newState: OnlineUserState): OnlineUser {
        val newUser = user.copy(
            state = newState
        )

        onlineUsers[user.user.id] = newUser

        return newUser
    }
}