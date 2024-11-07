package com.hacklord.routing

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Request {
    val code: Int

    @Serializable
    data class DrawLine(
        override val code: Int = 100,
        val pos: Int
    ): Request
}