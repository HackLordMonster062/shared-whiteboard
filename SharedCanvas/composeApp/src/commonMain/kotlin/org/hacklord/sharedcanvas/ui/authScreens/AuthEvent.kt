package org.hacklord.sharedcanvas.ui.authScreens

sealed interface AuthEvent {
    data class UsernameChanged(val newValue: String) : AuthEvent
    data class PasswordChanged(val newValue: String) : AuthEvent
    data object Login : AuthEvent
    data object Signup : AuthEvent
    data object Connect : AuthEvent
}
