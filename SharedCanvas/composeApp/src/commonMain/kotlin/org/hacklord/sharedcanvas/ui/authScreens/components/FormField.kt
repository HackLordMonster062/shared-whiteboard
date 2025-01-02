package org.hacklord.sharedcanvas.ui.authScreens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.FieldLine
import sharedcanvas.composeapp.generated.resources.GochiHand_Regular
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun FormField(
    value: String,
    onChange: (newValue: String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    val fontFamily = FontFamily(Font(Res.font.GochiHand_Regular))

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontSize = 24.sp
            ),
            decorationBox = { innerTextField -> Box {
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        fontFamily = fontFamily,
                        fontSize = 24.sp
                    )
                }

                innerTextField()
            } },
            visualTransformation = VisualTransformation.None,
        )

        Image(
            painter = painterResource(Res.drawable.FieldLine),
            contentDescription = null
        )
    }
}