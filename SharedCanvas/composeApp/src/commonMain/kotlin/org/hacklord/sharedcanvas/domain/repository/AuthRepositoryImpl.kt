package org.hacklord.sharedcanvas.domain.repository

import com.russhwolf.settings.Settings
import io.ktor.client.call.body
import org.hacklord.sharedcanvas.domain.api.AuthAPI
import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest
import org.hacklord.sharedcanvas.domain.api.auth.AuthResult
import org.hacklord.sharedcanvas.domain.api.auth.TokenResponse

class AuthRepositoryImpl(
    val api: AuthAPI,
    private val settings: Settings
) : AuthRepository {
    override suspend fun signUp(request: AuthRequest): AuthResult<Unit> {
        return try {
            val response = api.signUp(
                request = request
            )

            if (response.status.value in 400..499)
                return AuthResult.Unauthorized()

            login(
                request = request
            )
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun login(request: AuthRequest): AuthResult<Unit> {
        return try {
            val response = api.login(request)

            if (response.status.value in 400..499)
                return AuthResult.Unauthorized()

            val token: TokenResponse = response.body()

            settings.putString("jwt", token.token)

            AuthResult.Authorized()
        } catch (e: Exception) {
            println(e)
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = settings.getStringOrNull("jwt") ?: return AuthResult.Unauthorized()
            val response = api.authenticate("Bearer $token")

            if (response.status.value in 400..499)
                return AuthResult.Unauthorized()

            AuthResult.Authorized()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}