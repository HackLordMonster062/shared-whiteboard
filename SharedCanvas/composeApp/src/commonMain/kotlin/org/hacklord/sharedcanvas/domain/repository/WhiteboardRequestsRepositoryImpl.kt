package org.hacklord.sharedcanvas.domain.repository

import org.hacklord.sharedcanvas.domain.event.WhiteboardResponse
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.WhiteboardRequest
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager

class WhiteboardRequestsRepositoryImpl : RequestsRepository<WhiteboardResponse, WhiteboardRequest> {
    override fun getResponsesFlow(): Flow<WhiteboardResponse> {
        return CommunicationManager.getResponsesFlow()
                .mapNotNull { Json.decodeFromString<WhiteboardResponse>(it.readText()) }
    }

    override suspend fun sendRequest(request: WhiteboardRequest) {
        CommunicationManager.session?.outgoing?.send(
            Frame.Text(Json.encodeToString<WhiteboardRequest>(request))
        )
    }
}