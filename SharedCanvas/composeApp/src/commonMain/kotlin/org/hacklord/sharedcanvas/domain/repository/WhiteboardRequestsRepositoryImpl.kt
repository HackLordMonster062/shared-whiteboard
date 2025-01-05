package org.hacklord.sharedcanvas.domain.repository

import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.WhiteboardRequest
import org.hacklord.sharedcanvas.domain.event.WhiteboardResponse
import org.hacklord.sharedcanvas.domain.event.whiteboardRequestModule
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager

class WhiteboardRequestsRepositoryImpl : RequestsRepository<WhiteboardResponse, WhiteboardRequest> {
    override fun getResponsesFlow(): Flow<WhiteboardResponse> {
        return CommunicationManager.getResponsesFlow()
                .mapNotNull { Json.decodeFromString<WhiteboardResponse>(it.readText()) }
    }

    override suspend fun sendRequest(request: WhiteboardRequest) {
        val json = Json {
            serializersModule = whiteboardRequestModule
            classDiscriminator = "code"
        }

        val str: String = json.encodeToString<WhiteboardRequest>(request)
        println("Whiteboard request: $str")

        CommunicationManager.session?.outgoing?.send(
            Frame.Text(json.encodeToString<WhiteboardRequest>(request))
        )
    }
}