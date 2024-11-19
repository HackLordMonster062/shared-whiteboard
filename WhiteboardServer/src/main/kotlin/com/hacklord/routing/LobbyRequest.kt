package com.hacklord.routing

sealed interface LobbyRequest {
    data object GetWhiteboards : LobbyRequest
    data class EnterWhiteboard(val boardID: Long) : LobbyRequest
}