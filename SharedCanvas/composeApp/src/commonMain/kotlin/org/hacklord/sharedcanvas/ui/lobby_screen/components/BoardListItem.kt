package org.hacklord.sharedcanvas.ui.lobby_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hacklord.sharedcanvas.components.Whiteboard

@Composable
fun BoardListItem(
    boardData: Whiteboard,
    modifier: Modifier = Modifier
) {
    Column {
        Text(boardData.id)
        Text(boardData.name)
    }
}