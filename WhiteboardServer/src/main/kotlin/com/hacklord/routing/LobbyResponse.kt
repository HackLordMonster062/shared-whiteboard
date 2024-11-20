package com.hacklord.routing

import com.hacklord.components.Whiteboard
import kotlinx.serialization.Serializable

@Serializable
sealed interface LobbyResponse {
    val code: Int

    @Serializable
    data class BoardList(
        override val code: Int = 100,
        val boards: List<Whiteboard>
    ) : LobbyResponse

    @Serializable
    data class EnterBoard(
        override val code: Int = 101,
        val boardInfo: Whiteboard
    ) : LobbyResponse

    @Serializable
    data class DeleteBoard(
        override val code: Int = 102
    ) : LobbyResponse

    @Serializable
    data class Error(
        override val code: Int = -1,
        val message: String
    ) : LobbyResponse
}