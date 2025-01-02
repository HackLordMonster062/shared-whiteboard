package org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.authScreens.components.FormField
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyEvent
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.Submit

@Composable
fun CreateBoardScreen(
    state: CreateBoardState,
    onEvent: (event: LobbyEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
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
                .width(150.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onEvent(LobbyEvent.CreateWhiteboard)
                }
        )
    }
}