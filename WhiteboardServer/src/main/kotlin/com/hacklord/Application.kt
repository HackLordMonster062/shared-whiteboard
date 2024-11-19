package com.hacklord

import com.hacklord.config.TokenConfig
import com.hacklord.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val config = TokenConfig(
        issuer = "http://0.0.0.0:8080",
        audience = "users",
        expiresIn = 2629746000L, // 1 month
        secret = System.getenv("JWT_SECRET") ?: "default_secret"
    )

    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureSecurity(config)
}
