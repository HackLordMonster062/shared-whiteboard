package org.hacklord.sharedcanvas.ui.whiteboard_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.UiEvent
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.AddUserPanel
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.ColorItem
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.DrawingCanvas
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.EraserItem
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.WidthSlider
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.AddUserIcon
import sharedcanvas.composeapp.generated.resources.ExitIcon
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun WhiteboardScreen(
    state: WhiteboardState,
    onEvent: (event: WhiteboardEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit,
    uiEventFlow: Flow<UiEvent>
) {
    LaunchedEffect(key1 = true) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> {}
            }
        }
    }

    Column {
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

    Row {
        IconButton(
            onClick = { onEvent(WhiteboardEvent.Exit) },
            modifier = Modifier
                .background(
                    Color(237, 237, 237),
                    shape = RoundedCornerShape(
                        bottomEnd = 16.dp
                    )
                )
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ExitIcon),
                contentDescription = "Exit"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onEvent(WhiteboardEvent.ToggleAddUserMenu) },
            modifier = Modifier
                .background(
                    Color(237, 237, 237),
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp
                    )
                )
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.AddUserIcon),
                contentDescription = "Add User"
            )
        }
    }

    if (state.isAddUserMenuOpen) {
        AddUserPanel(
            state,
            onEvent
        )
    }
}