package com.hacklord.di

import com.hacklord.components.auth.JwtTokenService
import com.hacklord.dataSources.UserDataSourceImpl
import com.hacklord.dataSources.WhiteboardDataSourceImpl
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val serverModule = module {
    single {
        KMongo.createClient(
            connectionString = "mongodb://localhost:27017"
        ).coroutine.getDatabase("whiteboardDB")
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
}