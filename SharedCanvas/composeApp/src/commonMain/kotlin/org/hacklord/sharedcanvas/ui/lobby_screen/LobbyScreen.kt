package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.ui.Route
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.ExitIcon
import sharedcanvas.composeapp.generated.resources.Logo
import sharedcanvas.composeapp.generated.resources.Res
import kotlin.math.min

@Composable
fun LobbyScreen(
    currState: LobbyState,
    onEvent: (event: LobbyEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = painterResource(Res.drawable.ExitIcon),
        contentDescription = null,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onEvent(LobbyEvent.Logout)
                onNavigate(Route.Login)
            }
    )

    Column {
        Image(
            painter = painterResource(Res.drawable.Logo),
            contentDescription = "Logo",
            modifier = Modifier
        )

        LazyColumn {
            for (i in 0..currState.boards.size step 3) {
                Row {
                    for (board in currState.boards.subList(i, min(i + 3, currState.boards.size))) {

                    }
                }
            }
        }
    }
}