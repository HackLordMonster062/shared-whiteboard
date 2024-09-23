package com.hacklord.settings

import java.awt.Color

class ValidValues {
    companion object {
        val lineWidth = IntRange(1, 4)
        val colors = IntRange(0, 6)
    }
}

//  Possible colors

// Color(0x000000),
// Color(0xFFFFFF),
// Color(0xFF0000),
// Color(0x0C07FF),
// Color(0x00FF0A),
// Color(0xFAFF00),
// Color(0xFF11E7),