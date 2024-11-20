package com.hacklord.di

import com.hacklord.components.auth.JwtTokenService
import com.hacklord.config.TokenConfig
import com.hacklord.dataSources.UserDataSourceImpl
import com.hacklord.dataSources.WhiteboardDataSourceImpl
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val serverModule = module {
    val db = KMongo.createClient(
        connectionString = "mongodb://localhost:27017"
    ).coroutine.getDatabase("whiteboardDB")
    single {
        db
    }
    single<UserDataSource> {
        UserDataSourceImpl(
            get()
        )
    }
    single<WhiteboardDataSource> {
        WhiteboardDataSourceImpl(
            get()
        )
    }
    single {
        JwtTokenService()
    }
    single {
        OnlineBoardsManager(
            get()
        )
    }

    factory {
        TokenConfig(
            issuer = "http://0.0.0.0:8080",
            audience = "users",
            expiresIn = 2629746000L, // 1 month
            secret = System.getenv("JWT_SECRET") ?: "default_secret"
        )
    }
}