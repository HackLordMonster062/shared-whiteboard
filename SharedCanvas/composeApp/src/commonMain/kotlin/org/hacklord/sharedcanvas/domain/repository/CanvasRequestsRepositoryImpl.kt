package org.hacklord.sharedcanvas.domain.repository

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.CanvasRequest
import org.hacklord.sharedcanvas.domain.event.CanvasResponse
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager.client

class CanvasRequestsRepositoryImpl(
    client: HttpClient
) : CanvasRequestsRepository {
    private var session: WebSocketSession? = null

    override fun getResponsesFlow(): Flow<CanvasResponse> {
        return flow {
            session = client.webSocketSession {
                url("ws://127.0.0.1:1234/connect")
            }
            val responses = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<CanvasResponse>(it.readText()) }

            emitAll(responses)
        }
    }

    override suspend fun sendRequest(request: CanvasRequest) {
        session?.outgoing?.send(
            Frame.Text(Json.encodeToString<CanvasRequest>(request))
        )
    }
}