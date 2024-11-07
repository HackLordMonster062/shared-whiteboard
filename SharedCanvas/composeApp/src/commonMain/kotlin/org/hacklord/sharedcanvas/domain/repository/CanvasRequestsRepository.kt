package org.hacklord.sharedcanvas.domain.repository

import org.hacklord.sharedcanvas.domain.event.CanvasAction

interface CanvasRequestsRepository {
    suspend fun sendRequest(request: CanvasAction)
}