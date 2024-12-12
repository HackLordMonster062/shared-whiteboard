package org.hacklord.sharedcanvas.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

val webSocketsModule = module {
    single {
        HttpClient(CIO) {
            install(WebSockets)
        }
    }

}