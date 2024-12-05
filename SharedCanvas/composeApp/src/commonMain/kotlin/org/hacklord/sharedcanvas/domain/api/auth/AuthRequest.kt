package org.hacklord.sharedcanvas.domain.api.auth

data class AuthRequest(
    val username: String,
    val password: String
)
