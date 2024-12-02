package org.hacklord.sharedcanvas

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.hacklord.sharedcanvas.di.appModule
import org.koin.core.context.startKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SharedCanvas",
    ) {
        startKoin {
            printLogger()
            modules(appModule)
        }

        App()
    }
}