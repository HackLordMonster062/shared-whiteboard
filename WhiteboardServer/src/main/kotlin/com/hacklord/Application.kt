package com.hacklord

import com.hacklord.di.serverModule
import com.hacklord.plugins.*
import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    startKoin {
        printLogger()
        modules(serverModule)
    }

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSockets()
    configureRouting()
    configureSerialization()
    configureMonitoring()
}
