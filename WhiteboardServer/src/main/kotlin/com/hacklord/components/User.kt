package com.hacklord.components

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val id: Long,
)
