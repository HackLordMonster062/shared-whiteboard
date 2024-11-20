package com.hacklord.routing

import kotlinx.serialization.Serializable

@Serializable
sealed interface GeneralRequest {
    val code: Int

    @Serializable
    data class Connect(
        val token: String,
        override val code: Int = 1
    ) : GeneralRequest
}