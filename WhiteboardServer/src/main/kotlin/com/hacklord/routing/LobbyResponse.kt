package com.hacklord.routing

import com.hacklord.components.Whiteboard
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface LobbyResponse {
    @Serializable
    @SerialName("100")
    data class BoardList(
        val boards: List<Whiteboard>
    ) : LobbyResponse

    @Serializable
    @SerialName("101")
    data class EnterBoard(
        val boardInfo: Whiteboard
    ) : LobbyResponse

    @Serializable
    @SerialName("102")
    data object DeleteBoard : LobbyResponse

    @Serializable
    @SerialName("-1")
    data class Error(
        val message: String
    ) : LobbyResponse
}