package com.hacklord.components

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User(
    val name: String,
    val password: String,
    val id: String = ObjectId().toString(),
)

