package org.hacklord.sharedcanvas.domain.event

import kotlinx.serialization.Serializable

@Serializable
sealed interface GeneralRequest {
    @Serializable
    data class Connect(
        val token: String
    ) : GeneralRequest
}