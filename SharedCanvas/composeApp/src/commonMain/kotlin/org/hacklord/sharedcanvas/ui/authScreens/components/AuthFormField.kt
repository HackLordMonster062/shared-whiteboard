package org.hacklord.sharedcanvas.ui.authScreens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.FieldLine
import sharedcanvas.composeapp.generated.resources.GochiHand_Regular
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun AuthFormField(
    value: String,
    onChange: (newValue: String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    val fontFamily = FontFamily(Font(Res.font.GochiHand_Regular))

    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(
                fontFamily = fontFamily
            ),
            placeholder = {
                Text(
                    text = hint,
                    fontFamily = fontFamily
                )
            }
        )

        Image(
            painter = painterResource(Res.drawable.FieldLine),
            contentDescription = null
        )
    }
}