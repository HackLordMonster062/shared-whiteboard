package org.hacklord.sharedcanvas.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hacklord.sharedcanvas.AppConstants
import org.hacklord.sharedcanvas.domain.api.auth.AuthRequest

class AuthAPIImpl : AuthAPI {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation)
    }

    override suspend fun signUp(request: AuthRequest): HttpResponse {
        return client.post(AppConstants.URL + "signup") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }
    }

    override suspend fun login(request: AuthRequest): HttpResponse {
        return client.post(AppConstants.URL + "login") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }
    }

    override suspend fun authenticate(token: String): HttpResponse {
        return client.get(AppConstants.URL + "authenticate") {
            headers {
                append("Authorization", token)
            }
        }
    }
}