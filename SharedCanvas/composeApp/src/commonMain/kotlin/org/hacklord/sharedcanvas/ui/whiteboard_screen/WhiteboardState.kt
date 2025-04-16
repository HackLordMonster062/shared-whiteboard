package org.hacklord.sharedcanvas.ui.whiteboard_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.domain.data.User

data class WhiteboardState(
    val drawingMode: DrawingMode = DrawingMode.Draw(0),
    val currWidth: Int = 2,
    val lines: SnapshotStateList<Line> = mutableStateListOf(),
    val isAddUserMenuOpen: Boolean = false,
    val currUsernameToAdd: String = "",
    val users: List<User> = listOf()
)
