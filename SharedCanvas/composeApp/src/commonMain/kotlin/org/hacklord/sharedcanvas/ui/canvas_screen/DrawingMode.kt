package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.ui.graphics.Color

sealed interface DrawingMode {
    data class Draw(val color: Color) : DrawingMode
    data object Erase : DrawingMode
}