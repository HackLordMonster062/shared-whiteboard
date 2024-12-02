package org.hacklord.sharedcanvas.ui.canvas_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt


@Composable
fun DrawingCanvas(color: Color, lineWidth: Dp, modifier: Modifier = Modifier) {
    var path by remember { mutableStateOf(Path()) }

    var paths by remember { mutableStateOf<List<Pair<Path, Color>>>(listOf()) }

    var currPoint by remember { mutableStateOf(Offset(0f, 0f)) }

    BoxWithConstraints(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .pointerInput(true, color) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            path.moveTo(offset.x, offset.y)
                            currPoint = offset
                        },
                        onDrag = { change, _ ->
                            change.consume()

                            if (currPoint.distanceTo(change.position) >= 5f) {
                                currPoint.let { prev ->
                                    val midPoint = Offset(
                                        (prev.x + change.position.x) / 2,
                                        (prev.y + change.position.y) / 2
                                    )

                                    path = Path().apply {
                                        addPath(path)
                                        quadraticBezierTo(prev.x, prev.y, midPoint.x, midPoint.y)
                                    }
                                }
                                currPoint = change.position
                            }
                        },
                        onDragEnd = {
                            paths = paths + (Path().apply {addPath(path)} to color)
                            path = Path()
                        }
                    )
                }
        ) {
            paths.forEach { path ->
                drawPath(
                    path = path.first,
                    color = path.second,
                    style = Stroke(
                        width = lineWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    )
                )
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = lineWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                )
            )
        }
    }
}

fun Offset.distanceTo(other: Offset): Float {
    val x = other.x - this.x
    val y = other.y - this.y

    return sqrt(x * x + y * y)
}
