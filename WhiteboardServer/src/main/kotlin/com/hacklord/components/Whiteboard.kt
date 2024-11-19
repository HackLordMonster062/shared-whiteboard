package com.hacklord.components

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Whiteboard(
    val name: String,
    val creator: User,
    val userWhitelist: Set<String> = setOf(),
    val lines: List<Line> = listOf(),
    val currLineId: Long = 0,
    val id: String = ObjectId().toString(),
)