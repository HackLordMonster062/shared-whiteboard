package org.hacklord.sharedcanvas.ui.canvas_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.Dp
import sharedcanvas.composeapp.generated.resources.EraserIcon
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun EraserItem(
    isSelected: Boolean,
    diameter: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f
    )
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = painterResource(Res.drawable.EraserIcon),
        contentDescription = null,
        modifier = modifier
            .width(diameter)
            .height(diameter)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
    )
}