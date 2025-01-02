package org.hacklord.sharedcanvas.ui.whiteboard_screen

sealed interface DrawingMode {
    data class Draw(val color: Int) : DrawingMode
    data object Erase : DrawingMode
}