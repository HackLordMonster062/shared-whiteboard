package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.ui.Route
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.ExitIcon
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun LobbyScreen(
    onEvent: () -> Unit,
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

                onNavigate(Route.Login)
            }
    )
}