package org.hacklord.sharedcanvas.domain.api

import io.ktor.client.statement.HttpResponse
import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest

interface AuthAPI {
    suspend fun signUp(
        request: AuthRequest
    ): HttpResponse

    suspend fun login(
        request: AuthRequest
    ): HttpResponse

    suspend fun authenticate(
        token: String
    ): HttpResponse
}