package com.hacklord.components

import java.awt.Point

data class Line(
    val id: Long?,
    val color: Int,
    val width: Int,
    val vertices: List<Point> = listOf(),
)
