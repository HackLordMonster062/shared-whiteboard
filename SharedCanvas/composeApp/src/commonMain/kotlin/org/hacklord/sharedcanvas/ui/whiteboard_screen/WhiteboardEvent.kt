package org.hacklord.sharedcanvas.ui.whiteboard_screen

import org.hacklord.sharedcanvas.components.Point

sealed interface WhiteboardEvent {
    data class AddLine(val line: List<Point>) : WhiteboardEvent
    data class RemoveLine(val id: String) : WhiteboardEvent
    data class SetColor(val color: Int) : WhiteboardEvent
    data object SetEraser : WhiteboardEvent
    data class SetWidth(val width: Int) : WhiteboardEvent
}
