package org.hacklord.sharedcanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasScreen
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasState
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.sqrt

@Composable
@Preview
fun App() {
    MaterialTheme {
        //MainCanvas()
        CanvasScreen(
            CanvasState(
                currColor = Color.Black,
                currWidth = 1
            )
        ) { }
    }
}

@Composable
fun MainCanvas(modifier: Modifier = Modifier) {
    var path by remember { mutableStateOf(Path()) }

    val paint = Paint().apply {
        isAntiAlias = true
    }

    var currPoint by remember { mutableStateOf(Offset(0f, 0f)) }

    BoxWithConstraints(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
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
