package org.hacklord.sharedcanvas.components

data class Line(
    val id: Long,
    val color: Int,
    val width: Int,
    val vertices: List<Point> = listOf(),
)