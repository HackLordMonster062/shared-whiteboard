package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.ui.graphics.Color

data class CanvasState(
    val currColor: Color = Color.Black,
    val currWidth: Int = 1
)
