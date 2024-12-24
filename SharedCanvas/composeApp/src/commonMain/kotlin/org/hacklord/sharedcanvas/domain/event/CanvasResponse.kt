package org.hacklord.sharedcanvas.domain.event

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface CanvasResponse {
    @Serializable
    @SerialName("200")
    data class DrawLine(
        val lineId: Long
    ) : CanvasResponse

    @Serializable
    @SerialName("201")
    data object EraseLine : CanvasResponse

    @Serializable
    @SerialName("202")
    data object ExitBoard : CanvasResponse

    @Serializable
    @SerialName("-1")
    data class Error(
        val message: String
    ) : CanvasResponse
}