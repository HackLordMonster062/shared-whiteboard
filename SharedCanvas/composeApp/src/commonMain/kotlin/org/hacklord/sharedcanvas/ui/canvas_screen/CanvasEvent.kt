package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.ui.graphics.Color
import org.hacklord.sharedcanvas.components.Line

sealed interface CanvasEvent {
    data class AddLine(val line: Line) : CanvasEvent
    data class RemoveLine(val id: Long) : CanvasEvent
    data class SetColor(val color: Color) : CanvasEvent
    data class SetWidth(val width: Int) : CanvasEvent
}
