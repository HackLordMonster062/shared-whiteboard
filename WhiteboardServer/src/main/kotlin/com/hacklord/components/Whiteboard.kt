package com.hacklord.components

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Whiteboard(
    val name: String,
    val creator: String,
    val userWhitelist: Set<String> = setOf(),
    val lines: List<Line> = listOf(),
    @BsonId val id: String = ObjectId().toString(),
)