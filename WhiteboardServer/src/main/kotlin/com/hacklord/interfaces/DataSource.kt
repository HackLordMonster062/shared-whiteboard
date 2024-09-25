package com.hacklord.interfaces

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface DataSource {
    fun push(data: Serializable) {
        Json.encodeToString(data)
    }

    fun pull() {

    }
}