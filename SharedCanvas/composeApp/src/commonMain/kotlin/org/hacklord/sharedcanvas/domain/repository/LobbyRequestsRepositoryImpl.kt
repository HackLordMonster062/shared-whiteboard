package org.hacklord.sharedcanvas.domain.repository

import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager

class LobbyRequestsRepositoryImpl : RequestsRepository<LobbyResponse, LobbyRequest> {
    override fun getResponsesFlow(): Flow<LobbyResponse> {
        return CommunicationManager.getResponsesFlow()
            .mapNotNull { Json.decodeFromString<LobbyResponse>(it.readText()) }
    }

    override suspend fun sendRequest(request: LobbyRequest) {
        CommunicationManager.session?.outgoing?.send(
            Frame.Text(Json.encodeToString<LobbyRequest>(request))
        )
    }
}