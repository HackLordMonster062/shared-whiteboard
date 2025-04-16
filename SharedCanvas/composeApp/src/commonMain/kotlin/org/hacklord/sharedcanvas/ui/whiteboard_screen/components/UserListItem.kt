package org.hacklord.sharedcanvas.ui.whiteboard_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import org.hacklord.sharedcanvas.domain.data.User
import org.jetbrains.compose.resources.Font
import sharedcanvas.composeapp.generated.resources.GochiHand_Regular
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontFamily = FontFamily(Font(Res.font.GochiHand_Regular))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = user.name,
            fontFamily = fontFamily,
            fontSize = 24.sp
        )
    }
}