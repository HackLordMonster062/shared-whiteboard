package org.hacklord.sharedcanvas.ui

sealed interface UiEvent {
    data object PopBackStack : UiEvent
    data class Navigate(val route: Route) : UiEvent
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent
}