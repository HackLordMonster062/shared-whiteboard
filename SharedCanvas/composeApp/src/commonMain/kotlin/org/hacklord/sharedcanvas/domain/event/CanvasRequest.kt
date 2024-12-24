package org.hacklord.sharedcanvas.domain.event

import org.hacklord.sharedcanvas.components.Line

sealed interface CanvasRequest {
    data class AddLine(val line: Line) : CanvasRequest
    data class RemoveLine(val id: String) : CanvasRequest
}
