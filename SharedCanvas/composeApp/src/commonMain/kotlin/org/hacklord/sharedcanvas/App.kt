package org.hacklord.sharedcanvas

import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.authScreens.login.LoginScreen
import org.hacklord.sharedcanvas.ui.authScreens.signup.SignupScreen
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyScreen
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyViewModel
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardScreen
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        MaterialTheme {
            var screen: Route by remember { mutableStateOf(Route.Login) }

            val onNavigate: (newRoute: Route) -> Unit = { newRoute ->
                screen = newRoute
            }

            LaunchedEffect(true) {
                CommunicationManager.onConnectionClosed = {
                    onNavigate(Route.Login)
                }
            }

            Crossfade(targetState = screen) { currentState ->
                when (currentState) {
                    is Route.Login -> {
                        val viewModel = koinInject<AuthViewModel>()

                        LoginScreen(
                            authState = viewModel.state,
                            authResults = viewModel.authResults,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                    is Route.Signup -> {
                        val viewModel = koinInject<AuthViewModel>()

                        SignupScreen(
                            authState = viewModel.state,
                            authResults = viewModel.authResults,
                            onEvent = viewModel::onEvent,
                            onNavigate = onNavigate
                        )
                    }
                    is Route.Lobby -> {
                        val viewModel = koinInject<LobbyViewModel>()

                        val lobbyOnNavigate = { route: Route ->
                            println("clear")
                            viewModel.clear()
                            onNavigate(route)
                        }

                        LobbyScreen(
                            state = viewModel.lobbyState,
                            createBoardState = viewModel.createBoardState,
                            onEvent = viewModel::onEvent,
                            onNavigate = lobbyOnNavigate,
                            uiEventFlow = viewModel.uiEvent
                        )
                    }
                    is Route.Whiteboard -> {
                        val viewModel = koinInject<WhiteboardViewModel>(
                            parameters = { parametersOf(currentState.board) }
                        )

                        val whiteboardOnNavigate = { route: Route ->
                            onNavigate(route)
                            viewModel.clear()
                        }

                        WhiteboardScreen(
                            state = viewModel.whiteboardState,
                            onEvent = viewModel::onEvent,
                            onNavigate = whiteboardOnNavigate,
                            uiEventFlow = viewModel.uiEvent
                        )
                    }
                }
            }
        }
    }
}
