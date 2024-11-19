package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteboardRequest {
    val code: Int

    @Serializable
    data class DrawLine(
        override val code: Int = 200,
        val line: Line
    ): WhiteboardRequest

    @Serializable
    data class EraseLine(
        override val code: Int = 201,
        val lineId: Long
    ) : WhiteboardRequest

    @Serializable
    data class ExitBoard(
        override val code: Int = 203
    ) : WhiteboardRequest
}