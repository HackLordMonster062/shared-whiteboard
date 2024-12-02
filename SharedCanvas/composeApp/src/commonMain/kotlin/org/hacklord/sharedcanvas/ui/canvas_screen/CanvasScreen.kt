package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.ui.canvas_screen.components.ColorItem
import org.hacklord.sharedcanvas.ui.canvas_screen.components.DrawingCanvas

@Composable
fun CanvasScreen(
    state: CanvasState,
    onEvent: (event: CanvasEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        DrawingCanvas(
            canvasState = state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            AppConstants.COLORS.forEach { color ->
                ColorItem(
                    color,
                    state.currColor == color,
                    60.dp,
                    onClick = {
                        onEvent(CanvasEvent.SetColor(color))
                    }
                )
                Spacer(Modifier.width(15.dp))
            }
        }
    }
}