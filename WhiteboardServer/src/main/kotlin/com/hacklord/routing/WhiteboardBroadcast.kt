package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface WhiteboardBroadcast : WhiteboardResponse {
    @Serializable
    @SerialName("250")
    data class DrawBroadcast(
        val line: Line
    ) : WhiteboardBroadcast

    @Serializable
    @SerialName("251")
    data class EraseBroadcast(
        val lineID: String
    ) : WhiteboardBroadcast
}