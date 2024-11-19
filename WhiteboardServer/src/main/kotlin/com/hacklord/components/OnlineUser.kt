package com.hacklord.components

import io.ktor.websocket.*

data class OnlineUser(
    val user: User,
    val state: OnlineUserState,
    val session: WebSocketSession
)
