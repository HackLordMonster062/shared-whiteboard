package org.hacklord.sharedcanvas.components

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Float,
    val y: Float
)
