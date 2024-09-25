package com.hacklord.components

import kotlinx.serialization.Serializable

@Serializable
data class Whiteboard(
    val id: Long,
    val name: String,
    val creator: User,
    val userWhitelist: Set<Long> = setOf(),
    val lines: List<Line> = listOf(),
    val currLineId: Long = 0,
)