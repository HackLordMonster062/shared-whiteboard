package org.hacklord.sharedcanvas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jdk.internal.net.http.common.Log
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import sharedcanvas.composeapp.generated.resources.Res
import sharedcanvas.composeapp.generated.resources.compose_multiplatform
import kotlin.math.sqrt

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainCanvas()
    }
}

@Composable
fun MainCanvas() {
    var path by remember { mutableStateOf(Path()) }

    val paint = Paint().apply {
        isAntiAlias = true
    }

    var currPoint by remember { mutableStateOf(Offset(0f, 0f)) }

    BoxWithConstraints {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .size(this.maxWidth, this.maxHeight)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            path.moveTo(offset.x, offset.y)
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            if (currPoint.distanceTo(change.position) >= 5f) {
                                path = Path().apply {
                                    addPath(path)
                                    lineTo(change.position.x, change.position.y)
                                }

                                currPoint = change.position
                            }
                        }
                    )
                }
        ) {
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                )
            )
        }

        Button(
            onClick = {
                path.reset()
            }
        ) {
            Text("Clear")
        }
    }
}

fun Offset.distanceTo(other: Offset): Float {
    val x = other.x - this.x
    val y = other.y - this.y

    return sqrt(x * x + y * y)
}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWeight: Dp = 1.dp
)