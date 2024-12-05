package org.hacklord.sharedcanvas.domain.api

import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest
import org.hacklord.sharedcanvas.domain.api.auth.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAPI {
    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST("login")
    suspend fun login(
        @Body request: AuthRequest
    ): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}