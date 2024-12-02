package org.hacklord.sharedcanvas.components

import androidx.compose.ui.graphics.Color

data class Line(
    val color: Color,
    val width: Int,
    val vertices: List<Point> = listOf(),
    val id: Long? = null,
)