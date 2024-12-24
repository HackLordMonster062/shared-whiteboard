package org.hacklord.sharedcanvas.ui.lobby_screen

sealed interface LobbyEvent {
    data class EnterCanvas(val id: String): LobbyEvent
    data object Logout : LobbyEvent
}
