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
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasEvent
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasState
import kotlin.math.sqrt

@Composable
fun DrawingCanvas(
    canvasState: CanvasState,
    onEvent: (CanvasEvent) -> Unit,
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
                .pointerInput(true, canvasState.currColor) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentLine = listOf(Point(offset.x, offset.y))
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val lastPoint = currentLine.lastOrNull()
                            if (lastPoint != null && Offset(lastPoint.x, lastPoint.y).distanceTo(change.position) >= 5f) {
                                currentLine = currentLine + Point(change.position.x, change.position.y)
                            }
                        },
                        onDragEnd = {
                            onEvent(CanvasEvent.AddLine(currentLine))
                            currentLine = listOf()
                        }
                    )
                }
        ) {
            canvasState.lines.forEach { line ->
                drawLine(line, this)
            }

            if (currentLine.isNotEmpty()) {
                val tempLine = Line(
                    color = canvasState.currColor,
                    width = canvasState.currWidth,
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
        color = line.color,
        style = Stroke(
            width = AppConstants.WIDTHS[line.width].toFloat(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
