package com.hacklord.plugins

import com.auth0.jwt.JWTVerifier
import com.hacklord.authentication.authenticate
import com.hacklord.authentication.getSecretInfo
import com.hacklord.authentication.login
import com.hacklord.authentication.signup
import com.hacklord.components.auth.JwtTokenService
import com.hacklord.config.TokenConfig
import com.hacklord.interfaces.UserDataSource
import com.hacklord.interfaces.WhiteboardDataSource
import com.hacklord.managers.OnlineBoardsManager
import com.hacklord.routing.connection
import com.hacklord.security.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting(
    userDataSource: UserDataSource = get<UserDataSource>(),
    whiteboardDataSource: WhiteboardDataSource = get<WhiteboardDataSource>(),
    onlineBoardsManager: OnlineBoardsManager = get<OnlineBoardsManager>(),
    hashingService: HashingService = get<HashingService>(),
    tokenService: JwtTokenService = get<JwtTokenService>(),
    tokenConfig: TokenConfig = get<TokenConfig>(),
    jwtVerifier: JWTVerifier = get<JWTVerifier>()
) {
    routing {
        login(
            userDataSource,
            hashingService,
            tokenService,
            tokenConfig
        )

        signup(
            userDataSource,
            hashingService
        )

        authenticate()
        getSecretInfo()
        connection(
            userDataSource,
            whiteboardDataSource,
            onlineBoardsManager,
            jwtVerifier
        )
    }
}
