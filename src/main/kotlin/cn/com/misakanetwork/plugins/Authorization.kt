package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.dao.Auth
import cn.com.misakanetwork.tools.PasswordEncryption
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import org.ktorm.dsl.*
import java.util.*
import kotlin.random.Random.Default.nextInt

fun Application.authorization() {
    routing {
        post("/login") {
            val info = call.receive<UserInfo>()
            val user = database.from(Auth).select(Auth.userName)
                .where {
                    (Auth.password eq PasswordEncryption.getEncryptedPassword(info.password, "112321321")) and
                            (Auth.userName eq info.userName)
                }
            if (user.rowSet.size() != 1) {
                throw AuthenticationException()
            }
//            this.context.
            val token = JWT.create()
//                .withIssuer(environment.config.property("jwt.issuer").getString())
                .withIssuer("http://0.0.0.0:8080/")
                .withClaim("username", info.userName)
                .withExpiresAt(Date(System.currentTimeMillis() + 600000))
//                .sign(Algorithm.HMAC256(environment.config.property("jwt.secret").getString()))
                .sign(Algorithm.HMAC256("secret"))
            call.respond(mutableMapOf("key" to token))
        }
    }

}

@Serializable
data class UserInfo(val userName: String, val password: String)
