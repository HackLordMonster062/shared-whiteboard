package org.hacklord.sharedcanvas.ui.whiteboard_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.SliderBackground
import sharedcanvas.composeapp.generated.resources.SliderHandle

@Composable
fun WidthSlider(
    value: Int,
    steps: List<Int>,
    onChange: (value: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(Size.Zero) }
    var currStep by remember { mutableStateOf(value) }

    Image(
        painter = painterResource(Res.drawable.SliderBackground),
        contentDescription = null,
        modifier = modifier
            .onSizeChanged { size = it.toSize() * .95f }
            .pointerInput(true) {
                detectTapGestures(
                    onPress = { position ->
                        currStep = getStep(steps.size, position.x, size.width)

                        onChange(currStep)
                    }
                )
            }
            .pointerInput(true) {
                detectDragGestures (
                    onDrag = { change, _ ->
                        change.consume()

                        currStep = getStep(steps.size, change.position.x, size.width)
                    },
                    onDragEnd = {
                        onChange(currStep)
                    }
                )
            }
    )

    Image(
        painter = painterResource(Res.drawable.SliderHandle),
        contentDescription = null,
        modifier = Modifier
            .offset {
                val stepSize = size.width / (steps.size - 1)
                val pos = currStep * stepSize + stepSize / 4
                IntOffset(x = -pos.toInt(), y = 0)
            }
    )
}

fun getStep(steps: Int, position: Float, maxWidth: Float): Int {
    val stepSize = maxWidth / (steps - 1)

    val fixedPos = Math.clamp(position, 0f, maxWidth)

    return ((fixedPos + stepSize / 2f) / stepSize).toInt()
}