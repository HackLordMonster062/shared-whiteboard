package com.hacklord.plugins

import com.hacklord.routing.connection
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        connection()
    }
}
