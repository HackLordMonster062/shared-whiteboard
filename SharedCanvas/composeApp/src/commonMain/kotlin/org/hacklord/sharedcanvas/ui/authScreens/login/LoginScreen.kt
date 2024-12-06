package org.hacklord.sharedcanvas.ui.authScreens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.ui.authScreens.AuthEvent
import org.hacklord.sharedcanvas.ui.authScreens.AuthState
import org.hacklord.sharedcanvas.ui.authScreens.components.AuthForm
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Background
import sharedcanvas.composeapp.generated.resources.LoginForm
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun LoginScreen(
    authState: AuthState,
    onEvent: (event: AuthEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(Res.drawable.Background),
            contentDescription = null
        )

        AuthForm(
            authState = authState,
            onEvent = onEvent,
            background = painterResource(Res.drawable.LoginForm),
            modifier = Modifier
                .fillMaxSize(.45f)
                .offset(x = maxWidth * .25f, y = maxHeight * .2f)
        )
    }
}