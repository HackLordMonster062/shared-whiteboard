package org.hacklord.sharedcanvas.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Line(
    val id: Long,
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWeight: Dp = 1.dp
)