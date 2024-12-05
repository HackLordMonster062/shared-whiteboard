package org.hacklord.sharedcanvas.ui.login

sealed interface LoginEvent {
    data class UsernameChanged(val newValue: String) : LoginEvent
    data class PasswordChanged(val newValue: String) : LoginEvent
}
