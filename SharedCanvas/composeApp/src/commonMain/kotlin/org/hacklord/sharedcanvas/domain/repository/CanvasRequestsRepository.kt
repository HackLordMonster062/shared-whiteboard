package org.hacklord.sharedcanvas.domain.repository

import kotlinx.coroutines.flow.Flow
import org.hacklord.sharedcanvas.domain.event.CanvasRequest
import org.hacklord.sharedcanvas.domain.event.CanvasResponse

interface CanvasRequestsRepository {
    suspend fun sendRequest(request: CanvasRequest)
    fun getResponsesFlow(): Flow<CanvasResponse>
}