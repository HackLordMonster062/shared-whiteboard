package com.hacklord.components

import io.ktor.websocket.*

data class OnlineUser(
    val user: User,
    val currBoard: Long,
    val session: WebSocketSession
)
