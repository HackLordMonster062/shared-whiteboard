package org.hacklord.sharedcanvas.ui.canvas_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.SliderBackground

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
        modifier = Modifier
            .onSizeChanged { size = it.toSize() }
            .pointerInput(true) {
                detectDragGestures (
                    onDrag = { change, _ ->
                        change.consume()

                        currStep = (change.position.x / size.width * steps.size).toInt()
                    }
                )
            }
    )
}