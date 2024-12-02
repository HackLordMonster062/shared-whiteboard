package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.domain.event.CanvasAction
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepository

class CanvasViewModel(
    private val repository: CanvasRequestsRepository,
    initLines: List<Line>
) : ViewModel() {
    private var _canvasState by mutableStateOf(CanvasState())
    val canvasState get() = _canvasState

    init {
        _canvasState.lines.addAll(initLines)
    }

    fun onEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.AddLine -> {
                val newLine = Line(
                    _canvasState.currColor,
                    _canvasState.currWidth,
                    event.line
                )

                _canvasState.lines.add(newLine)

                viewModelScope.launch {
                    repository.sendRequest(CanvasAction.AddLine(newLine))
                    //TODO: Receive the new line's id
                }
            }
            is CanvasEvent.RemoveLine -> {
                _canvasState.lines.removeIf { it.id == event.id }

                viewModelScope.launch {
                    repository.sendRequest(CanvasAction.RemoveLine(event.id))
                }
            }
            is CanvasEvent.SetColor -> {
                _canvasState = _canvasState.copy(currColor = event.color)
            }
            is CanvasEvent.SetWidth -> {
                _canvasState = _canvasState.copy(currWidth = event.width)
            }
        }
    }
}