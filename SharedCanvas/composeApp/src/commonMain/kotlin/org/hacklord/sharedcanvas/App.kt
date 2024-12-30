package org.hacklord.sharedcanvas

import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.authScreens.login.LoginScreen
import org.hacklord.sharedcanvas.ui.authScreens.signup.SignupScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyScreen
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        MaterialTheme {
            var screen: Route by remember { mutableStateOf(Route.Login) }

            val onNavigate: (newRoute: Route) -> Unit = { newRoute ->
                screen = newRoute
            }

            Crossfade(targetState = screen) { currentState ->
                when (currentState) {
                    is Route.Login -> {
                        val viewModel = koinViewModel<AuthViewModel>()

                        LoginScreen(
                            authState = viewModel.state,
                            authResults = viewModel.authResults,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                    is Route.Signup -> {
                        val viewModel = koinViewModel<AuthViewModel>()

                        SignupScreen(
                            authState = viewModel.state,
                            authResults = viewModel.authResults,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                    is Route.Lobby -> {
                        val viewModel = koinViewModel<LobbyViewModel>()

                        LobbyScreen(
                            state = viewModel.lobbyState,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                    is Route.Whiteboard -> {
                        val viewModel = koinViewModel<CanvasViewModel>()

                        CanvasScreen(
                            state = viewModel.canvasState,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                }
            }
        }
    }
}
