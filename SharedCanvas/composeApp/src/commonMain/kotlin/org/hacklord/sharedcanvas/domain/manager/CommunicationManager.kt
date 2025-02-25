package org.hacklord.sharedcanvas.domain.manager

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.shareIn

object CommunicationManager {
    var session: WebSocketSession? = null

    private var _responseFlow: Flow<Frame.Text>? = null

    private var client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }

    suspend fun initialize() {
        session = client.webSocketSession {
            url("ws://127.0.0.1:1234/connect")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getResponsesFlow(): Flow<Frame.Text> {
        _responseFlow = _responseFlow ?: getInnerResponsesFlow()
            .shareIn(GlobalScope, SharingStarted.Eagerly, replay = 0)

        return _responseFlow!!
    }

    private fun getInnerResponsesFlow(): Flow<Frame.Text> {
        return session!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .cancellable()
    }

    suspend fun close() {
        session?.close()
        _responseFlow = null
        session = null
    }
}