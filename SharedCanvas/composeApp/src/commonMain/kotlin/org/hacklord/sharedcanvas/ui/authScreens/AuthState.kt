package org.hacklord.sharedcanvas.ui.authScreens

data class AuthState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = ""
)
