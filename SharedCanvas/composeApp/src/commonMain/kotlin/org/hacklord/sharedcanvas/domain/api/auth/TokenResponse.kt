package org.hacklord.sharedcanvas.domain.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
