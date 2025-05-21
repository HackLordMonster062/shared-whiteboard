package org.hacklord.sharedcanvas.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val id: String
)
