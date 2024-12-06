package org.hacklord.sharedcanvas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.hacklord.sharedcanvas.ui.authScreens.AuthState
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.authScreens.login.LoginScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        //MainCanvas()
        val viewModel = koinInject<AuthViewModel>()

        LoginScreen(
            authState = AuthState(
                isLoading = false,
                username = "",
                password = ""
            ),
            onEvent = viewModel::onEvent
        )
    }
}
