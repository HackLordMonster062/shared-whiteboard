package org.hacklord.sharedcanvas.ui.canvas_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
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
    private val lines: SnapshotStateList<Line> = mutableStateListOf()

    var currColor by mutableStateOf(Color.Black)

    init {
        lines.addAll(initLines)
    }

    fun onEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.AddLine -> {
                lines.add(event.line)

                viewModelScope.launch {
                    repository.sendRequest(CanvasAction.AddLine(event.line))
                }
            }
            is CanvasEvent.RemoveLine -> {
                lines.removeIf { it.id == event.id }

                viewModelScope.launch {
                    repository.sendRequest(CanvasAction.RemoveLine(event.id))
                }
            }
            is CanvasEvent.SetColor -> {
                currColor = event.color
            }
        }
    }
}