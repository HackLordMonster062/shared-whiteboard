package com.hacklord.routing

import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteboardResponse {
    val code: Int

    @Serializable
    data class DrawLine(
        override val code: Int = 200,
        val lineId: Long
    ) : WhiteboardResponse

    @Serializable
    data class EraseLine(
        override val code: Int = 201,
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