package org.hacklord.sharedcanvas.ui.authScreens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import org.hacklord.sharedcanvas.domain.api.auth.AuthResult
import org.hacklord.sharedcanvas.ui.authScreens.AuthEvent
import org.hacklord.sharedcanvas.ui.authScreens.AuthState
import org.hacklord.sharedcanvas.ui.authScreens.components.AuthForm
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Background
import sharedcanvas.composeapp.generated.resources.LoginForm
import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.SignupLink

@Composable
fun LoginScreen(
    authState: AuthState,
    authResults: Flow<AuthResult<Unit>>,
    onEvent: (event: AuthEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(authState) {
        authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    //TODO: Navigate to lobby
                }
                is AuthResult.Unauthorized -> {
                    // Show error

                    println("Not authorized")
                }
                is AuthResult.UnknownError -> {
                    // Show error

                    println("Unknown error")
                }
            }
        }
    }

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
            onSubmitEvent = AuthEvent.Login,
            background = painterResource(Res.drawable.LoginForm),
            modifier = Modifier
                .fillMaxSize(.45f)
                .offset(x = maxWidth * .25f, y = maxHeight * .2f)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = maxHeight * .01f)
        ) {
            Image(
                painter = painterResource(Res.drawable.SignupLink),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        // Navigate to signup
                    }
            )
        }
    }
}