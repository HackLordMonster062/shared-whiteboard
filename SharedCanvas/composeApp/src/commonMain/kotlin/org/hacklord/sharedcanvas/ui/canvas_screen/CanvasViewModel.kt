package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.domain.event.CanvasRequest
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepository

class CanvasViewModel(
    private val repository: CanvasRequestsRepository,
    initLines: List<Line>
) : ViewModel() {
    private var _canvasState by mutableStateOf(CanvasState())
    val canvasState get() = _canvasState

    init {
        _canvasState.lines.addAll(initLines)

        viewModelScope.launch {
            repository.getResponsesFlow().collect {

            }
        }
    }

    fun onEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.AddLine -> {
                if (_canvasState.drawingMode is DrawingMode.Draw) {
                    val newLine = Line(
                        (_canvasState.drawingMode as DrawingMode.Draw).color,
                        _canvasState.currWidth,
                        event.line
                    )

                    _canvasState.lines.add(newLine)

                    viewModelScope.launch {
                        repository.sendRequest(CanvasRequest.AddLine(newLine))
                        //TODO: Receive the new line's id
                    }
                }
            }
            is CanvasEvent.RemoveLine -> {
                _canvasState.lines.removeIf { it.id == event.id }

                viewModelScope.launch {
                    repository.sendRequest(CanvasRequest.RemoveLine(event.id))
                }
            }
            is CanvasEvent.SetColor -> {
                _canvasState = _canvasState.copy(
                    drawingMode = DrawingMode.Draw(color = event.color)
                )
            }
            is CanvasEvent.SetEraser -> {
                _canvasState = _canvasState.copy(
                    drawingMode = DrawingMode.Erase
                )
            }
            is CanvasEvent.SetWidth -> {
                _canvasState = _canvasState.copy(currWidth = event.width)
            }
        }
    }
}