package org.hacklord.sharedcanvas.ui.canvas_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ColorItem(
    color: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f)

    Box(
        modifier = modifier
            .background(color = color)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
    )
}