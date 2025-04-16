package org.hacklord.sharedcanvas.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val password: String,
    val salt: String,
    val id: String
)
