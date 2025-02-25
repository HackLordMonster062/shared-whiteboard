package com.hacklord.authentication

import com.hacklord.components.User
import com.hacklord.components.auth.AuthRequest
import com.hacklord.components.auth.AuthResponse
import com.hacklord.components.auth.JwtTokenService
import com.hacklord.components.auth.TokenClaim
import com.hacklord.config.TokenConfig
import com.hacklord.interfaces.UserDataSource
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signup(
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPasswordInvalid = request.password.length < 8

        if (areFieldsBlank || isPasswordInvalid) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val user = User(
            request.username,
            request.password, // TODO: Hash
        )

        val userId = userDataSource.insertUser(user)

        userId ?: kotlin.run {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.login(
    userDataSource: UserDataSource,
    tokenService: JwtTokenService,
    config: TokenConfig
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username) ?: run {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val invalidPassword = user.password == request.password // TODO: check hash

        if (!invalidPassword) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val token = tokenService.generate(
            config = config,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )

        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                token = token
            )
        )
    }
}


fun Route.authenticate() {
    println(authenticate {
        get("authenticate") {
            println(call.pathParameters)
            call.respond(HttpStatusCode.OK)
        }
    })
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)

            call.respond(HttpStatusCode.OK)
        }
    }
}