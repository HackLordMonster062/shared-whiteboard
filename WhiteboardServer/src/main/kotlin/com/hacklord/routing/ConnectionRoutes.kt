package com.hacklord.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.connection() {
    webSocket("/connect") {
        send(
            Frame.Text(
                true, "welcome".toByteArray()
            )
        )
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