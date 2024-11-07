package com.hacklord.routing

import com.hacklord.managers.OnlineBoardsManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

val users = ConcurrentHashMap<String, WebSocketSession>()

fun Route.connection() {
    webSocket("/connect") {
        OnlineBoardsManager

        incoming.consumeEach {
            if (it is Frame.Binary) {
                send(
                    Frame.Binary(
                        true, "bin".toByteArray()
                    )
                )
            } else {
                send(
                    Frame.Text(
                        true, "text".toByteArray()
                    )
                )
            }
        }
    }
}