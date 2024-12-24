package org.hacklord.sharedcanvas.di

import com.russhwolf.settings.Settings
import org.hacklord.sharedcanvas.domain.api.AuthAPI
import org.hacklord.sharedcanvas.domain.api.AuthAPIImpl
import org.hacklord.sharedcanvas.domain.event.CanvasRequest
import org.hacklord.sharedcanvas.domain.event.CanvasResponse
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.repository.AuthRepository
import org.hacklord.sharedcanvas.domain.repository.AuthRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.LobbyRequestsRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<RequestsRepository<CanvasResponse, CanvasRequest>> {
        CanvasRequestsRepositoryImpl()
    }
    single<RequestsRepository<LobbyResponse, LobbyRequest>> {
        LobbyRequestsRepositoryImpl()
    }
    single<AuthAPI> {
        AuthAPIImpl()
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