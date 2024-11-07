package org.hacklord.sharedcanvas.domain.event

import org.hacklord.sharedcanvas.components.Line

sealed interface CanvasAction {
    data class AddLine(val line: Line) : CanvasAction
    data class RemoveLine(val id: Long) : CanvasAction
}
