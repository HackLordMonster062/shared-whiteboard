package com.hacklord.routing

import kotlinx.serialization.Serializable

@Serializable
sealed interface LobbyRequest {
    val code: Int

    @Serializable
    data class GetWhiteboards(
        override val code: Int = 100
    ) : LobbyRequest

    @Serializable
    data class EnterWhiteboard(
        override val code: Int = 101,
        val boardID: String
    ) : LobbyRequest

    @Serializable
    data class CreateWhiteboard(
        override val code: Int = 102,
        val name: String
    ) : LobbyRequest

    @Serializable
    data class DeleteWhiteboard(
        override val code: Int = 103,
        val boardID: String
    ) : LobbyRequest
}