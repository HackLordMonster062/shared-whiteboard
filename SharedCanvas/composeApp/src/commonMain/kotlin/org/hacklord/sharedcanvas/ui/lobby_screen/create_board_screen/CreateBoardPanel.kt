package org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.authScreens.components.FormField
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyEvent
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.CreateBoardBG
import sharedcanvas.composeapp.generated.resources.CreateBoardTitle
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.Submit

@Composable
fun CreateBoardPanel(
    state: CreateBoardState,
    onEvent: (event: LobbyEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Popup(
        onDismissRequest = { onEvent(LobbyEvent.CreateWhiteboardToggle) },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,

            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onEvent(LobbyEvent.CreateWhiteboardToggle) }
        ) {
            Image(
                painter = painterResource(Res.drawable.CreateBoardBG),
                contentDescription = null,
                modifier = modifier
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Image(
                    painter = painterResource(Res.drawable.CreateBoardTitle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(40.dp)
                )

                FormField(
                    value = state.name,
                    onChange = { newValue ->
                        onEvent(LobbyEvent.WhiteboardNameChanged(newValue))
                    },
                    hint = "New Whiteboard"
                )

                Image(
                    painter = painterResource(Res.drawable.Submit),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onEvent(LobbyEvent.CreateWhiteboard)
                        }
                )
            }
        }
    }
}