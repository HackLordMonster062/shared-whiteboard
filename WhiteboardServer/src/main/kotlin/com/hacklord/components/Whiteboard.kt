package com.hacklord.components

data class Whiteboard(
    val id: Int,
    val name: String,
    val creator: User,
    val users: List<User> = listOf(),
    val lines: List<Line> = listOf(),
    val currLineId: Long = 0,
)