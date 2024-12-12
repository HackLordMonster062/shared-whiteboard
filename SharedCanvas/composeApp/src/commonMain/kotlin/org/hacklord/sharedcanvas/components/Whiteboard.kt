package org.hacklord.sharedcanvas.components

import kotlinx.serialization.Serializable

@Serializable
data class Whiteboard(
    val name: String,
    val creator: String,
    val userWhitelist: Set<String> = setOf(),
    val lines: List<Line> = listOf(),
    val currLineId: Long = 0,
    val id: String
)