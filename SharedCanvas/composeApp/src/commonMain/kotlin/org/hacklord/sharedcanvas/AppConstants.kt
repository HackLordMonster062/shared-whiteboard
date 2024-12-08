package org.hacklord.sharedcanvas

import androidx.compose.ui.graphics.Color

object AppConstants {
    val URL = "http://127.0.0.1:1234/"

    val COLORS = listOf(
        Color.Black,
        Color.White,
        Color.Red,
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color(0xFFFF11E7)
    )

    val WIDTHS = listOf(
        2,
        5,
        8,
        11,
        14
    )

    const val DRAWING_BUFFER = 5f
}