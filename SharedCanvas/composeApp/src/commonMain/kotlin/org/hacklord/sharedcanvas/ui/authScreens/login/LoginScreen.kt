package org.hacklord.sharedcanvas.ui.authScreens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
    Image(
        painter = painterResource(Res.drawable.Background),
        contentDescription = null
    )

    Box(
        Modifier
            .fillMaxSize()
    ) {
        AuthForm(
            authState = authState,
            onEvent = onEvent,
            background = painterResource(Res.drawable.LoginForm),
            modifier = modifier
                .fillMaxSize(.45f)
        )
    }
}