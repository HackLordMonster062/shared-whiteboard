package com.hacklord.routing

sealed interface LobbyRequest {
    val code: Int

    data class GetWhiteboards(
        override val code: Int = 100
    ) : LobbyRequest
    data class EnterWhiteboard(
        override val code: Int = 101,
        val boardID: String
    ) : LobbyRequest
    data class CreateWhiteboard(
        override val code: Int = 102,
        val name: String
    ) : LobbyRequest
    data class DeleteWhiteboard(
        override val code: Int = 103,
        val boardID: String
    ) : LobbyRequest
}