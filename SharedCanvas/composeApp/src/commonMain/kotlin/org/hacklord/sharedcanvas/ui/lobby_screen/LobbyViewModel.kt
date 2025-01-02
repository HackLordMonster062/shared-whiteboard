package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.repository.GeneralRequestsRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.UiEvent
import org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen.CreateBoardState

class LobbyViewModel(
    private val repository: RequestsRepository<LobbyResponse, LobbyRequest>,
    generalRepository: GeneralRequestsRepositoryImpl,
    settings: Settings
) : ViewModel() {
    private var _lobbyState by mutableStateOf(LobbyState())
    val lobbyState get() = _lobbyState

    private var _createBoardState by mutableStateOf(CreateBoardState(""))
    val createBoardState get() = _createBoardState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            println("Starting Lobby: Sending token...")
            val token = settings.getString("jwt", "")

            generalRepository.sendAuthenticate(token)

            repository.sendRequest(LobbyRequest.GetWhiteboards)

            repository.getResponsesFlow().collect { response ->
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
                    else -> { }
                }
            }
        }
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
                    // Log out
                }
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