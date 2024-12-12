package org.hacklord.sharedcanvas.ui.canvas_screen

sealed interface DrawingMode {
    data class Draw(val color: Int) : DrawingMode
    data object Erase : DrawingMode
}