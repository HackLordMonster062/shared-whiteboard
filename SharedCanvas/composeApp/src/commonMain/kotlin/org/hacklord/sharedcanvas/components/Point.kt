package org.hacklord.sharedcanvas.components

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Float,
    val y: Float
) {
    operator fun times(b: Float): Point {
        return Point(x * b, y * b)
    }
}
