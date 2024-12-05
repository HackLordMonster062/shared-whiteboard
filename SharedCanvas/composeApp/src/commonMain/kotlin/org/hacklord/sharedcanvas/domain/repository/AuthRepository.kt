package org.hacklord.sharedcanvas.domain.repository

import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest
import org.hacklord.sharedcanvas.domain.api.auth.AuthResult

interface AuthRepository {
    suspend fun signUp(request: AuthRequest): AuthResult<Unit>
    suspend fun login(request: AuthRequest): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}