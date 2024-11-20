package com.hacklord.plugins

import com.auth0.jwt.JWTVerifier
import com.hacklord.config.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get

fun Application.configureSecurity(
    config: TokenConfig = get<TokenConfig>(),
    jwtVerifier: JWTVerifier = get<JWTVerifier>()
) {
    val jwtRealm = "whiteboard"

    install(Authentication)
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                jwtVerifier
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}