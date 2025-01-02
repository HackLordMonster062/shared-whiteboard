package org.hacklord.sharedcanvas.ui.lobby_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.components.Whiteboard

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
        Text(boardData.id)
        Text(boardData.name)
    }
}