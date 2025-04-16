package org.hacklord.sharedcanvas.ui.whiteboard_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.components.Line
import org.hacklord.sharedcanvas.components.Whiteboard
import org.hacklord.sharedcanvas.domain.event.WhiteboardBroadcast
import org.hacklord.sharedcanvas.domain.event.WhiteboardRequest
import org.hacklord.sharedcanvas.domain.event.WhiteboardResponse
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.UiEvent
import java.util.UUID

class WhiteboardViewModel(
    private val repository: RequestsRepository<WhiteboardResponse, WhiteboardRequest>,
    initInfo: Whiteboard
) : ViewModel() {
    private var _whiteboardState by mutableStateOf(WhiteboardState())
    val whiteboardState get() = _whiteboardState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val job = viewModelScope.launch {
        repository.getResponsesFlow().collect { response ->
            when (response) {
                is WhiteboardResponse.ExitBoard -> {
                    _uiEvent.send(UiEvent.Navigate(Route.Lobby))
                }
                is WhiteboardResponse.GetAllUsers -> {
                    _whiteboardState = _whiteboardState.copy(users = response.users)
                }
                is WhiteboardBroadcast.DrawBroadcast -> {
                    _whiteboardState.lines.add(response.line)
                }
                is WhiteboardBroadcast.EraseBroadcast -> {
                    eraseLine(response.lineID)
                }
                else -> {}
            }
        }
    }

    init {
        _whiteboardState.lines.addAll(initInfo.lines)
    }

    fun clear() {
        job.cancel()
    }

    fun onEvent(event: WhiteboardEvent) {
        when (event) {
            is WhiteboardEvent.Exit -> {
                viewModelScope.launch {
                    repository.sendRequest(WhiteboardRequest.ExitBoard)
                }
            }
            is WhiteboardEvent.AddLine -> {
                if (_whiteboardState.drawingMode is DrawingMode.Draw) {
                    val newLine = Line(
                        id = UUID.randomUUID().toString(),
                        color = (_whiteboardState.drawingMode as DrawingMode.Draw).color,
                        width = _whiteboardState.currWidth,
                        vertices = event.line
                    )

                    _whiteboardState.lines.add(newLine)

                    viewModelScope.launch {
                        repository.sendRequest(WhiteboardRequest.DrawLine(newLine))
                    }
                }
            }
            is WhiteboardEvent.RemoveLine -> {
                eraseLine(event.id)

                viewModelScope.launch {
                    repository.sendRequest(WhiteboardRequest.EraseLine(event.id))
                }
            }
            is WhiteboardEvent.SetColor -> {
                _whiteboardState = _whiteboardState.copy(
                    drawingMode = DrawingMode.Draw(color = event.color)
                )
            }
            is WhiteboardEvent.SetEraser -> {
                _whiteboardState = _whiteboardState.copy(
                    drawingMode = DrawingMode.Erase
                )
            }
            is WhiteboardEvent.SetWidth -> {
                _whiteboardState = _whiteboardState.copy(currWidth = event.width)
            }
            is WhiteboardEvent.ToggleAddUserMenu -> {
                val currState = _whiteboardState.isAddUserMenuOpen
                _whiteboardState = _whiteboardState.copy(isAddUserMenuOpen = !currState)
            }
            is WhiteboardEvent.ChangeUsernameToAdd -> {
                _whiteboardState = _whiteboardState.copy(currUsernameToAdd = event.newValue)

                viewModelScope.launch {
                    repository.sendRequest(WhiteboardRequest.SearchUsers(event.newValue))
                }
            }
            is WhiteboardEvent.AddUser -> {
                viewModelScope.launch {
                    repository.sendRequest(WhiteboardRequest.AddUser(event.id))
                }
            }
        }
    }

    private fun eraseLine(id: String) {
        for (i in _whiteboardState.lines.indices) {
            if (_whiteboardState.lines[i].id == id) {
                _whiteboardState.lines.removeAt(i)
                break
            }
        }
    }
}