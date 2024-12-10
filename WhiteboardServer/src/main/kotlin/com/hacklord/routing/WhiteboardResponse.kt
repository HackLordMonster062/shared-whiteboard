package com.hacklord.routing

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface WhiteboardResponse {
    @Serializable
    @SerialName("200")
    data class DrawLine(
        val lineId: Long
    ) : WhiteboardResponse

    @Serializable
    @SerialName("201")
    data object EraseLine : WhiteboardResponse

    @Serializable
    @SerialName("202")
    data object ExitBoard : WhiteboardResponse

    @Serializable
    @SerialName("-1")
    data class Error(
        val message: String
    ) : WhiteboardResponse
}