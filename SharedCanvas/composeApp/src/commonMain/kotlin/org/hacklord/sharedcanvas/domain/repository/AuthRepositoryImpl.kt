package org.hacklord.sharedcanvas.domain.repository

import com.russhwolf.settings.Settings
import org.hacklord.sharedcanvas.domain.api.AuthAPI
import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest
import org.hacklord.sharedcanvas.domain.api.auth.AuthResult
import retrofit2.HttpException

class AuthRepositoryImpl(
    val api: AuthAPI,
    val settings: Settings
) : AuthRepository {
    override suspend fun signUp(request: AuthRequest): AuthResult<Unit> {
        return try {
            api.signUp(
                request = request
            )
            login(
                request = request
            )
        } catch (e: HttpException) {
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun login(request: AuthRequest): AuthResult<Unit> {
        return try {
            val response = api.login(request)

            settings.putString("jwt", response.token)

            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = settings.getStringOrNull("jwt") ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401)
                AuthResult.Unauthorized()
            else
                AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}