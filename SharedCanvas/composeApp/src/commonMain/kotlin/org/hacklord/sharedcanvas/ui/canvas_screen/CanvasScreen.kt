package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.MainCanvas
import org.hacklord.sharedcanvas.ui.canvas_screen.components.ColorItem

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel
) {
    Row {
        MainCanvas()
        Column {
            AppConstants.COLORS.forEach { color ->
                ColorItem(
                    color,
                    viewModel.currColor == color,
                    modifier = Modifier
                        .clickable {
                            viewModel.currColor = color
                        }
                )
            }
        }
    }
}