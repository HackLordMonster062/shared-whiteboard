package org.hacklord.sharedcanvas.ui

sealed interface Route {
    data object Login : Route
    data object Signup : Route
    data object Lobby : Route
    data class Whiteboard(val board: org.hacklord.sharedcanvas.components.Whiteboard) : Route
}