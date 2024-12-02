package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import org.hacklord.sharedcanvas.components.Line

data class CanvasState(
    val currColor: Color = Color.Black,
    val currWidth: Int = 5,
    val lines: SnapshotStateList<Line> = mutableStateListOf()
)
