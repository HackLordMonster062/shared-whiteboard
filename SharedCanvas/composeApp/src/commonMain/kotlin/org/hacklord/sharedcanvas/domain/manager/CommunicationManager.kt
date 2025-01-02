package org.hacklord.sharedcanvas.domain.manager

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow

object CommunicationManager {
    var session: WebSocketSession? = null

    private var client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun initialize() {
        session = client.webSocketSession {
            url("ws://127.0.0.1:1234/connect")
        }
    }

    fun getResponsesFlow(): Flow<Frame.Text> {
        return flow {
            val responses = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()

            emitAll(responses)
        }
    }

    suspend fun close() {
        session?.close()
        session = null
    }
}