package org.hacklord.sharedcanvas.ui.authScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest
import org.hacklord.sharedcanvas.domain.api.auth.AuthResult
import org.hacklord.sharedcanvas.domain.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _state by mutableStateOf(AuthState())
    val state get() = _state

    private val _authResult = Channel<AuthResult<Unit>>()
    val authResults = _authResult.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.UsernameChanged -> {
                _state = _state.copy(
                    username = event.newValue
                )
            }
            is AuthEvent.PasswordChanged -> {
                _state = _state.copy(
                    password = event.newValue
                )
            }
            is AuthEvent.Login -> {
                viewModelScope.launch {
                    _state = _state.copy(isLoading = true)

                    val result = authRepository.login(AuthRequest(
                        username = _state.username,
                        password = _state.password
                    ))

                    _authResult.send(result)

                    _state = _state.copy(isLoading = false)
                }
            }
            is AuthEvent.Signup -> {
                viewModelScope.launch {
                    _state = _state.copy(isLoading = true)

                    val result = authRepository.signUp(AuthRequest(
                        username = _state.username,
                        password = _state.password
                    ))

                    _authResult.send(result)

                    _state = _state.copy(isLoading = false)
                }
            }
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _state = _state.copy(isLoading = true)

            val result = authRepository.authenticate()

            _authResult.send(result)

            _state = _state.copy(isLoading = false)
        }
    }
}