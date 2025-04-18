package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.manager.CommunicationManager
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.UiEvent
import org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen.CreateBoardState

class LobbyViewModel(
    private val repository: RequestsRepository<LobbyResponse, LobbyRequest>
) : ViewModel() {
    private var _lobbyState by mutableStateOf(LobbyState())
    val lobbyState get() = _lobbyState

    private var _createBoardState by mutableStateOf(CreateBoardState(false, ""))
    val createBoardState get() = _createBoardState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val job: Job = viewModelScope.launch {
        repository.sendRequest(LobbyRequest.GetWhiteboards)

        repository.getResponsesFlow().collect { response ->
            println(response)

            when (response) {
                is LobbyResponse.BoardList -> {
                    println("Received boards")
                    _lobbyState.boards.clear()
                    _lobbyState.boards.addAll(response.boards)
                }

                is LobbyResponse.EnterBoard -> {
                    _uiEvent.send(
                        UiEvent.Navigate(
                            Route.Whiteboard(response.boardInfo)
                        )
                    )
                }

                is LobbyResponse.Error -> {
                    // TODO: Handle errors
                }

                else -> {}
            }
        }
    }

    fun clear() {
        job.cancel()
    }

    fun onEvent(event: LobbyEvent) {
        when (event) {
            is LobbyEvent.EnterWhiteboard -> {
                viewModelScope.launch {
                    repository.sendRequest(LobbyRequest.EnterWhiteboard(event.id))
                }
            }
            is LobbyEvent.Logout -> {
                viewModelScope.launch {
                    CommunicationManager.close()
                }
            }
            is LobbyEvent.CreateWhiteboardToggle -> {
                _createBoardState = _createBoardState.copy(isOpen = !_createBoardState.isOpen)
            }
            is LobbyEvent.WhiteboardNameChanged -> {
                _createBoardState = _createBoardState.copy(name = event.newValue)
            }
            is LobbyEvent.CreateWhiteboard -> {
                viewModelScope.launch {
                    repository.sendRequest(LobbyRequest.CreateWhiteboard(
                        name = _createBoardState.name
                    ))
                }
            }
        }
    }
}