package com.hacklord.components

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String
)
