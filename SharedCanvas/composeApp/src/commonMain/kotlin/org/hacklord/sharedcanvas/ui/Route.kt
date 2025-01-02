package org.hacklord.sharedcanvas.ui

sealed interface Route {
    data object Login : Route
    data object Signup : Route
    sealed interface Lobby : Route {
        data object BoardList : Lobby
        data object CreateBoard : Lobby
    }
    data class Whiteboard(val board: org.hacklord.sharedcanvas.components.Whiteboard) : Route
}