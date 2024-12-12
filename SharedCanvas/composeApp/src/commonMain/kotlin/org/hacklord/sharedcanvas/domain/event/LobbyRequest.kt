package org.hacklord.sharedcanvas.domain.event

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface LobbyRequest {
    @Serializable
    @SerialName("100")
    data object GetWhiteboards : LobbyRequest

    @Serializable
    @SerialName("101")
    data class EnterWhiteboard(
        val boardID: String
    ) : LobbyRequest

    @Serializable
    @SerialName("102")
    data class CreateWhiteboard(
        val name: String
    ) : LobbyRequest

    @Serializable
    @SerialName("103")
    data class DeleteWhiteboard(
        val boardID: String
    ) : LobbyRequest
}