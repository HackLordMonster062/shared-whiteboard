package com.hacklord.di

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
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
        connectionString = "mongodb+srv://hackmonster062:hacklord@cluster0.rxn6q.mongodb.net/"
    ).coroutine.getDatabase("whiteboardDB").also {
        println("Connected to database successfully.")
    }
    single<UserDataSource> {
        UserDataSourceImpl(
            db
        )
    }
    single<WhiteboardDataSource> {
        WhiteboardDataSourceImpl(
            db
        )
    }
    single {
        JwtTokenService()
    }
    single {
        OnlineBoardsManager(
            db
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

    factory {
        val config = get<TokenConfig>()
        JWT
            .require(Algorithm.HMAC256(config.secret))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
    }
}