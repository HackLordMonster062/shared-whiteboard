package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import org.hacklord.sharedcanvas.components.Line

data class CanvasState(
    val drawingMode: DrawingMode = DrawingMode.Draw(Color.Black),
    val currWidth: Int = 2,
    val lines: SnapshotStateList<Line> = mutableStateListOf()
)
