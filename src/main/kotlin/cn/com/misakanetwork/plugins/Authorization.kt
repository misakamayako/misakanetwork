package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.dao.Auth
import cn.com.misakanetwork.tools.PasswordEncryption
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import org.ktorm.dsl.*
import java.util.*
import kotlin.random.Random.Default.nextInt

fun Application.authorization() {
    routing {
        get("/login") {
            call.respondHtml(HttpStatusCode.OK) {
                body {
                    form(method = FormMethod.post,
                        action = "/login",
                        encType = FormEncType.applicationXWwwFormUrlEncoded) {
                        label {
                            +"name"
                        }
                        input(name = "userName") {
                            id = "userName"
                            placeholder = "userName"
                        }
                        label {
                            +"password"
                        }
                        input(name = "password", type = InputType.password) {
                            id = "password"
                            placeholder = "userName"
                        }
                        input(name = "id", type = InputType.hidden) {
                            value = nextInt(1000).toString()
                        }
                        button(type = ButtonType.button) {
                            +"提交"
                        }
                    }
                    script(src="https://cdn.bootcdn.net/ajax/libs/axios/0.21.1/axios.min.js"){}
                }
            }
        }
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
            val token = JWT.create()
                .withIssuer(environment.config.property("jwt.issuer").getString())
                .withClaim("username", info.userName)
                .withExpiresAt(Date(System.currentTimeMillis() + 600000))
                .sign(Algorithm.HMAC256(environment.config.property("jwt.secret").getString()))
            call.respond(mutableMapOf("key" to token))
        }
    }

}

@Serializable
data class UserInfo(val userName: String, val password: String)
