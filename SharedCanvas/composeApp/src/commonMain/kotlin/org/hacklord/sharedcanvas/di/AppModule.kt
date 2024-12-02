package org.hacklord.sharedcanvas.di

import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepository
import org.hacklord.sharedcanvas.domain.repository.CanvasRequestsRepositoryImpl
import org.hacklord.sharedcanvas.ui.canvas_screen.CanvasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CanvasRequestsRepository> {
        CanvasRequestsRepositoryImpl()
    }
    viewModel {
        CanvasViewModel(
            get(),
            listOf()
        )
    }
}