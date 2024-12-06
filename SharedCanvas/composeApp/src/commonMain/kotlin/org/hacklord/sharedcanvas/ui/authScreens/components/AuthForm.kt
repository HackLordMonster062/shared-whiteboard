package org.hacklord.sharedcanvas.ui.authScreens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import org.hacklord.sharedcanvas.ui.authScreens.AuthEvent
import org.hacklord.sharedcanvas.ui.authScreens.AuthState

@Composable
fun AuthForm(
    authState: AuthState,
    onEvent: (event: AuthEvent) -> Unit,
    background: Painter,
    modifier: Modifier = Modifier
) {
    Box(
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
                .fillMaxHeight(.7f)
        ) {
            AuthFormField(
                value = authState.username,
                onChange = { newValue ->
                    onEvent(AuthEvent.UsernameChanged(newValue))
                },
                hint = "Username",
                modifier = Modifier
                    .fillMaxWidth(.6f)
            )
            AuthFormField(
                value = authState.password,
                onChange = { newValue ->
                    onEvent(AuthEvent.PasswordChanged(newValue))
                },
                hint = "Password",
                modifier = Modifier
                    .fillMaxWidth(.6f)
            )
        }
    }
}