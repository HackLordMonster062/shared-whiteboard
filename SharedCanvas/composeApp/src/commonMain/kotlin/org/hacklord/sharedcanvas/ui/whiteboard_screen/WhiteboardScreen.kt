package org.hacklord.sharedcanvas.ui.whiteboard_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.ColorItem
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.DrawingCanvas
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.EraserItem
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.WidthSlider

@Composable
fun WhiteboardScreen(
    state: WhiteboardState,
    onEvent: (event: WhiteboardEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit
) {
    Column(
        //horizontalAlignment = Alignment.End
    ) {
        DrawingCanvas(
            whiteboardState = state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            modifier = Modifier
                .background(Color(237, 237, 237))
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppConstants.COLORS.forEachIndexed { index, color ->
                ColorItem(
                    color,
                    state.drawingMode is DrawingMode.Draw &&
                            state.drawingMode.color == index,
                    40.dp,
                    onClick = {
                        onEvent(WhiteboardEvent.SetColor(index))
                    }
                )
                Spacer(Modifier.width(15.dp))
            }

            EraserItem(
                isSelected = state.drawingMode is DrawingMode.Erase,
                diameter = 40.dp,
                onClick = {
                    onEvent(WhiteboardEvent.SetEraser)
                }
            )

            Spacer(
                Modifier
                    .weight(1f)
            )

            WidthSlider(
                value = state.currWidth,
                steps = AppConstants.WIDTHS,
                onChange = { newValue ->
                    onEvent(WhiteboardEvent.SetWidth(
                        newValue
                    ))
                }
            )
        }
    }
}