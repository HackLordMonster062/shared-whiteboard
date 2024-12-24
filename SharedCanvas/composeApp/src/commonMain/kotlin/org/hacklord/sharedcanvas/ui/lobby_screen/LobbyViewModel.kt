package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository

class LobbyViewModel(
    private val repository: RequestsRepository<LobbyResponse, LobbyRequest>
) : ViewModel() {
    private var _lobbyState by mutableStateOf(LobbyState())
    val lobbyState get() = _lobbyState

    init {
        viewModelScope.launch {
            repository.getResponsesFlow().collect { response ->
                when (response) {
                    is LobbyResponse.BoardList -> {
                        _lobbyState.boards.clear()
                        _lobbyState.boards.addAll(response.boards)
                    }
                    is LobbyResponse.Error -> {
                        // TODO: Handle errors
                    }
                    else -> { }
                }
            }

            repository.sendRequest(LobbyRequest.GetWhiteboards)
        }
    }

    fun onEvent(event: LobbyEvent) {
        when (event) {
            is LobbyEvent.EnterCanvas -> {
                viewModelScope.launch {
                    repository.sendRequest(LobbyRequest.EnterWhiteboard(event.id))
                }
            }
            is LobbyEvent.Logout -> {
                viewModelScope.launch {
                    // Log out
                }
            }
        }
    }
}