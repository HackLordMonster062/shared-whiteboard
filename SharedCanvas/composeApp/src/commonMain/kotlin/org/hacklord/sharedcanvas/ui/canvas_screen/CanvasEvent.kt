package org.hacklord.sharedcanvas.ui.canvas_screen

import org.hacklord.sharedcanvas.components.Point
import java.util.UUID

sealed interface CanvasEvent {
    data class AddLine(val line: List<Point>) : CanvasEvent
    data class RemoveLine(val id: String) : CanvasEvent
    data class SetColor(val color: Int) : CanvasEvent
    data object SetEraser : CanvasEvent
    data class SetWidth(val width: Int) : CanvasEvent
}
