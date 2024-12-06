package org.hacklord.sharedcanvas.di

import com.russhwolf.settings.Settings
import org.hacklord.sharedcanvas.domain.api.AuthAPI
import org.hacklord.sharedcanvas.domain.repository.AuthRepository
import org.hacklord.sharedcanvas.domain.repository.AuthRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepository
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepositoryImpl
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<CanvasRequestsRepository> {
        CanvasRequestsRepositoryImpl()
    }
    single<AuthAPI> {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }
    single {
        Settings()
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            get(),
            get()
        )
    }
    viewModel {
        CanvasViewModel(
            get(),
            listOf()
        )
    }
    viewModel {
        AuthViewModel(
            get()
        )
    }
}