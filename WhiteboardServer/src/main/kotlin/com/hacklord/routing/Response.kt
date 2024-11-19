package com.hacklord.routing

import com.hacklord.components.Line
import kotlinx.serialization.Serializable

@Serializable
sealed interface Response {
    val code: Int

    @Serializable
    data class DrawBroadcast(
        override val code: Int = 200,
        val line: Line
    ) : Response

    @Serializable
    data class EraseBroadcast(
        override val code: Int = 201,
        val lineID: Long
    ) : Response
}