package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.ui.graphics.Color
import org.hacklord.sharedcanvas.components.Line

sealed class CanvasEvent {
    data class AddLine(val line: Line) : CanvasEvent()
    data class RemoveLine(val id: Long) : CanvasEvent()
    data class SetColor(val id: Long, val color: Color) : CanvasEvent()
}
