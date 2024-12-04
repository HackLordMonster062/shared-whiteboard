package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.ui.graphics.Color
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.components.Point

sealed interface CanvasEvent {
    data class AddLine(val line: List<Point>) : CanvasEvent
    data class RemoveLine(val id: Long) : CanvasEvent
    data class SetColor(val color: Color) : CanvasEvent
    data object SetEraser : CanvasEvent
    data class SetWidth(val width: Int) : CanvasEvent
}
