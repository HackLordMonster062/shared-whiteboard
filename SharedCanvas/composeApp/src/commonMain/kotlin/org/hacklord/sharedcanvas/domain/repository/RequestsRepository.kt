package org.hacklord.sharedcanvas.domain.repository

import kotlinx.coroutines.flow.Flow

interface RequestsRepository<Res, Req> {
    fun getResponsesFlow(): Flow<Res>
    suspend fun sendRequest(request: Req)
}