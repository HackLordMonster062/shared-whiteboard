package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.hacklord.sharedcanvas.components.Whiteboard

data class LobbyState(
    val boards: SnapshotStateList<Whiteboard> = mutableStateListOf()
)
