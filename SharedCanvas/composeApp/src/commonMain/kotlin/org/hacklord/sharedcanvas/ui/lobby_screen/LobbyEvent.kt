package org.hacklord.sharedcanvas.ui.lobby_screen

sealed interface LobbyEvent {
    data class EnterWhiteboard(val id: String): LobbyEvent
    data object CreateWhiteboard: LobbyEvent
    data object CreateWhiteboardToggle: LobbyEvent
    data class WhiteboardNameChanged(val newValue: String): LobbyEvent
    data object Logout : LobbyEvent
}
