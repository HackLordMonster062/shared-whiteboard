package com.hacklord.components

import kotlinx.serialization.Serializable

@Serializable
data class Line(
    val id: Long?,
    val color: Int,
    val width: Int,
    val vertices: List<Point> = listOf(),
)

@Serializable
data class Point(
    val x: Int,
    val y: Int,
)