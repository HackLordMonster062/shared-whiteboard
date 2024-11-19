package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteboardRequest {
    val code: Int

    @Serializable
    data class DrawLine(
        override val code: Int = 100,
        val line: Line
    ): WhiteboardRequest

    @Serializable
    data class EraseLine(
        override val code: Int = 101,
        val lineId: Long
    ) : WhiteboardRequest
}