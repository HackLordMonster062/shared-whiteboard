package com.hacklord.components

import java.awt.Color
import java.awt.Point

data class Line(
    val color: Color,
    val width: Int,
    val vertices: List<Point> = listOf(),
)
