package org.hacklord.sharedcanvas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        //MainCanvas()
        val viewModel = koinInject<CanvasViewModel>()

        CanvasScreen(
            viewModel.canvasState,
            onEvent = viewModel::onEvent
        )
    }
}
