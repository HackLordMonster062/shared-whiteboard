package com.hacklord.managers

import com.hacklord.components.OnlineUser
import com.hacklord.components.OnlineUserState
import com.hacklord.components.User
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

object UserManager {
    val onlineUsers = ConcurrentHashMap<Long, OnlineUser>()

    fun connectUser(user: User, session: WebSocketSession): OnlineUser {
        val onlineUser = OnlineUser(
            user,
            OnlineUserState.InLobby,
            session
        )

        return onlineUser
    }
}