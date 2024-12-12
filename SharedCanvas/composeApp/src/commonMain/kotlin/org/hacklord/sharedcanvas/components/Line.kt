package org.hacklord.sharedcanvas.components

import kotlinx.serialization.Serializable

@Serializable
data class Line(
    val color: Int,
    val width: Int,
    val vertices: List<Point> = listOf(),
    val id: Long? = null,
)