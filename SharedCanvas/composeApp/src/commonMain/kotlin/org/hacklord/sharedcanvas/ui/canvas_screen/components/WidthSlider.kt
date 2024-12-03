package org.hacklord.sharedcanvas.ui.canvas_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
    Image(
        painter = painterResource(Res.drawable.SliderBackground),
        contentDescription = null,
        modifier = Modifier
            .pointerInput(true) {
                detectDragGestures (
                    onDrag = { change, _ ->

                    }
                )
            }
    )
}