package org.hacklord.sharedcanvas.ui.lobby_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.components.Whiteboard
import org.hacklord.sharedcanvas.ui.whiteboard_screen.components.drawLine
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.Frame
import sharedcanvas.composeapp.generated.resources.GochiHand_Regular
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun BoardListItem(
    boardData: Whiteboard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontFamily = FontFamily(Font(Res.font.GochiHand_Regular))

    Box {
        Image(
            painter = painterResource(Res.drawable.Frame),
            contentDescription = null,
            modifier = modifier
        )

        Column(
            modifier = modifier.clickable {
                onClick()
            }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5 / 3f)
                    .padding(5.dp)
                    .clipToBounds()
            ) {
                boardData.lines.forEach { line ->
                    drawLine(line, this, .2f)
                }
            }

            Text(
                boardData.name,
                fontFamily = fontFamily,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}