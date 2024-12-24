package org.hacklord.sharedcanvas.domain.repository

import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.CanvasRequest
import org.hacklord.sharedcanvas.domain.event.CanvasResponse
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager

class CanvasRequestsRepositoryImpl : RequestsRepository<CanvasResponse, CanvasRequest> {
    override fun getResponsesFlow(): Flow<CanvasResponse> {
        return CommunicationManager.getResponsesFlow()
                .mapNotNull { Json.decodeFromString<CanvasResponse>(it.readText()) }
    }

    override suspend fun sendRequest(request: CanvasRequest) {
        CommunicationManager.session?.outgoing?.send(
            Frame.Text(Json.encodeToString<CanvasRequest>(request))
        )
    }
}