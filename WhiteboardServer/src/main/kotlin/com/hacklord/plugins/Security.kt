package com.hacklord.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hacklord.config.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.get

fun Application.configureSecurity(
    config: TokenConfig = get<TokenConfig>()
) {
    val jwtRealm = "whiteboard"
    install(Authentication)
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}