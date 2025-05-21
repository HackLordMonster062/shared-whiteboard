package com.hacklord.components

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserEntity(
    val name: String,
    val password: String,
    val salt: String,
    @BsonId val id: String = ObjectId().toString(),
)

