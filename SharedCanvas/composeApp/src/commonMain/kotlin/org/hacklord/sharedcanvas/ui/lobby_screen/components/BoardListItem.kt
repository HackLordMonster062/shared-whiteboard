package org.hacklord.sharedcanvas.ui.lobby_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.components.Whiteboard
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.drawLine

@Composable
fun BoardListItem(
    boardData: Whiteboard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4/3f)
        ) {
            boardData.lines.forEach { line ->
                drawLine(line, this, .2f)
            }
        }

        Text(boardData.name)
    }
}