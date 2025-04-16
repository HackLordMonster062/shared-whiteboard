package org.hacklord.sharedcanvas.ui.authScreens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.ui.authScreens.AuthEvent
import org.hacklord.sharedcanvas.ui.authScreens.AuthState
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.Submit

@Composable
fun AuthForm(
    authState: AuthState,
    onEvent: (event: AuthEvent) -> Unit,
    onSubmitEvent: AuthEvent,
    background: Painter,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequesters = remember { List(2) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(
        modifier = modifier
    ) {
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = maxHeight * .35f,
                    bottom = maxHeight * .06f
                )
        ) {
            FormField(
                value = authState.username,
                onChange = { newValue ->
                    onEvent(AuthEvent.UsernameChanged(newValue))
                },
                hint = "Username",
                modifier = Modifier
                    .fillMaxWidth(.6f)
            )
            FormField(
                value = authState.password,
                onChange = { newValue ->
                    onEvent(AuthEvent.PasswordChanged(newValue))
                },
                hint = "Password",
                modifier = Modifier
                    .fillMaxWidth(.6f)
            )
            Spacer(modifier = Modifier
                .weight(.1f))
            Image(
                painter = painterResource(Res.drawable.Submit),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onEvent(onSubmitEvent)
                    }
            )
        }
    }
}