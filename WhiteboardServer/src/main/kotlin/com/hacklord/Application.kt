package com.hacklord

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.hacklord.di.serverModule
import com.hacklord.plugins.*
import io.ktor.server.application.*
import org.koin.core.context.startKoin
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger("org.mongodb.driver").level = Level.ERROR

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
