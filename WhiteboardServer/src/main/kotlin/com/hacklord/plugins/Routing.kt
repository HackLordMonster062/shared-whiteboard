package com.hacklord.plugins

import com.hacklord.authentication.authenticate
import com.hacklord.authentication.getSecretInfo
import com.hacklord.authentication.login
import com.hacklord.authentication.signup
import com.hacklord.components.auth.JwtTokenService
import com.hacklord.config.TokenConfig
import com.hacklord.interfaces.UserDataSource
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting(
    userDataSource: UserDataSource = get<UserDataSource>(),
    tokenService: JwtTokenService = get<JwtTokenService>(),
    tokenConfig: TokenConfig = get<TokenConfig>()
) {
    routing {
        login(
            userDataSource,
            tokenService,
            tokenConfig
        )

        signup(
            userDataSource
        )

        authenticate()
        getSecretInfo()
    }
}
