package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteboardResponse {
    val code: Int

    @Serializable
    data class DrawBroadcast(
        override val code: Int = 200,
        val line: Line
    ) : WhiteboardResponse

    @Serializable
    data class EraseBroadcast(
        override val code: Int = 201,
        val lineID: Long
    ) : WhiteboardResponse

    @Serializable
    data class ExitBoard(
        override val code: Int = 202
    ) : WhiteboardResponse

    @Serializable
    data class Error(
        override val code: Int = -1,
        val message: String
    ) : WhiteboardResponse
}