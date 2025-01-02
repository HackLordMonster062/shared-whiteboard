package org.hacklord.sharedcanvas.domain.repository

import io.ktor.websocket.Frame
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.domain.event.GeneralRequest
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager

class GeneralRequestsRepositoryImpl {
    suspend fun sendAuthenticate(token: String) {
        val str: String = Json.encodeToString(GeneralRequest.Connect(token = token))
        println("General request: $str")

        val channel = CommunicationManager.session?.outgoing ?: throw RuntimeException("No session.")

        channel.send(
            Frame.Text(str)
        )
    }
}