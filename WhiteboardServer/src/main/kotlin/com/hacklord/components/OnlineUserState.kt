package com.hacklord.components

sealed interface OnlineUserState {
    data object InLobby : OnlineUserState
    data class InWhiteboard(val boardId: String) : OnlineUserState
}