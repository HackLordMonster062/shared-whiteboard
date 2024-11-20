package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteboardBroadcast {
    val code: Int

    @Serializable
    data class DrawBroadcast(
        override val code: Int = 250,
        val line: Line
    ) : WhiteboardBroadcast

    @Serializable
    data class EraseBroadcast(
        override val code: Int = 251,
        val lineID: Long
    ) : WhiteboardBroadcast
}