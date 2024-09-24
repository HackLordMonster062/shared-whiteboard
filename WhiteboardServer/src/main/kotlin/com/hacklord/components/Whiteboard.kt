package com.hacklord.components

data class Whiteboard(
    val id: Long,
    val name: String,
    val creator: User,
    val whitelist: Set<Long> = setOf(),
    val lines: List<Line> = listOf(),
    val currLineId: Long = 0,
)