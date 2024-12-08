package org.hacklord.sharedcanvas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.authScreens.signup.SignupScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        MaterialTheme {
            //MainCanvas()
            val viewModel = koinInject<AuthViewModel>()

            SignupScreen(
                authState = viewModel.state,
                authResults = viewModel.authResults,
                onEvent = viewModel::onEvent
            )
        }
    }
}
