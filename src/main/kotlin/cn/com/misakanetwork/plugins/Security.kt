package cn.com.misakanetwork.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun Application.configureSecurity() {
	val secret = environment.config.property("jwt.secret").getString()
	val issuer = environment.config.property("jwt.issuer").getString()
	install(Authentication) {
		jwt("auth-jwt") {
			verifier(
				JWT
					.require(Algorithm.HMAC256(secret))
					.withIssuer(issuer)
					.build()
			)
			validate { credential ->
				if (credential.payload.getClaim("username").asString() == "misaka") {
					JWTPrincipal(credential.payload)
				} else {
					null
				}
			}
		}
	}
}
