package org.hacklord.sharedcanvas.ui.whiteboard_screen.components

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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.components.Point
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardEvent
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardState
import org.hacklord.sharedcanvas.ui.whiteboard_screen.DrawingMode
import kotlin.math.sqrt

@Composable
fun DrawingCanvas(
    whiteboardState: WhiteboardState,
    onEvent: (WhiteboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentLine by remember { mutableStateOf<List<Point>>(listOf()) }

    BoxWithConstraints(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .pointerInput(true, whiteboardState) {
                    when (whiteboardState.drawingMode) {
                        is DrawingMode.Draw -> detectDragGestures(
                                onDragStart = { offset ->
                                    currentLine = listOf(Point(offset.x, offset.y))
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    val lastPoint = currentLine.lastOrNull()
                                    if (lastPoint != null && Offset(
                                            lastPoint.x,
                                            lastPoint.y
                                        ).distanceTo(change.position) >= AppConstants.DRAWING_BUFFER
                                    ) {
                                        currentLine = currentLine + Point(
                                            change.position.x,
                                            change.position.y
                                        )
                                    }
                                },
                                onDragEnd = {
                                    onEvent(WhiteboardEvent.AddLine(currentLine))
                                    currentLine = listOf()
                                }
                            )
                        is DrawingMode.Erase -> detectDragGestures(
                                onDrag = { change, _ ->
                                    whiteboardState.lines.forEach { line ->
                                        line.vertices.forEach { point ->
                                            if (change.position.distanceTo(point) <= AppConstants.DRAWING_BUFFER) {
                                                onEvent(WhiteboardEvent.RemoveLine(line.id!!))
                                            }
                                        }
                                    }
                                }
                            )
                    }
                }
        ) {
            whiteboardState.lines.forEach { line ->
                drawLine(line, this)
            }

            if (currentLine.isNotEmpty() && whiteboardState.drawingMode is DrawingMode.Draw) {
                val tempLine = Line(
                    color = whiteboardState.drawingMode.color,
                    width = whiteboardState.currWidth,
                    vertices = currentLine
                )
                drawLine(tempLine, this)
            }
        }
    }
}

fun Offset.distanceTo(other: Offset): Float {
    val x = other.x - this.x
    val y = other.y - this.y

    return sqrt(x * x + y * y)
}

fun Offset.distanceTo(other: Point): Float {
    val x = other.x - this.x
    val y = other.y - this.y

    return sqrt(x * x + y * y)
}

fun drawLine(
    line: Line,
    drawScope: DrawScope
) {
    val path = Path().apply {
        if (line.vertices.isNotEmpty()) {
            moveTo(line.vertices.first().x, line.vertices.first().y)
            for (i in 1 until line.vertices.size) {
                val prev = line.vertices[i - 1]
                val curr = line.vertices[i]
                val midPoint = Point((prev.x + curr.x) / 2, (prev.y + curr.y) / 2)
                quadraticBezierTo(prev.x, prev.y, midPoint.x, midPoint.y)
            }
        }
    }

    drawScope.drawPath(
        path = path,
        color = AppConstants.COLORS[line.color],
        style = Stroke(
            width = AppConstants.WIDTHS[line.width].toFloat(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
