package org.hacklord.sharedcanvas.di

import org.hacklord.sharedcanvas.domain.event.WhiteboardResponse
import com.russhwolf.settings.Settings
import org.hacklord.sharedcanvas.domain.api.AuthAPI
import org.hacklord.sharedcanvas.domain.api.AuthAPIImpl
import org.hacklord.sharedcanvas.domain.event.LobbyRequest
import org.hacklord.sharedcanvas.domain.event.LobbyResponse
import org.hacklord.sharedcanvas.domain.event.WhiteboardRequest
import org.hacklord.sharedcanvas.domain.repository.AuthRepository
import org.hacklord.sharedcanvas.domain.repository.AuthRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.GeneralRequestsRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.LobbyRequestsRepositoryImpl
import org.hacklord.sharedcanvas.domain.repository.RequestsRepository
import org.hacklord.sharedcanvas.domain.repository.WhiteboardRequestsRepositoryImpl
import org.hacklord.sharedcanvas.ui.authScreens.AuthViewModel
import org.hacklord.sharedcanvas.ui.lobby_screen.LobbyViewModel
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<RequestsRepository<WhiteboardResponse, WhiteboardRequest>> {
        WhiteboardRequestsRepositoryImpl()
    }
    single<RequestsRepository<LobbyResponse, LobbyRequest>> {
        LobbyRequestsRepositoryImpl()
    }
    single<GeneralRequestsRepositoryImpl> {
        GeneralRequestsRepositoryImpl()
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
    viewModel { parameters ->
        WhiteboardViewModel(
            get(),
            parameters.get()
        )
    }
    viewModel {
        LobbyViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel {
        AuthViewModel(
            get()
        )
    }
}