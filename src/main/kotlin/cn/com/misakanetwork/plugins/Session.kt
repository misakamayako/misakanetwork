package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.sessions.*

fun Application.session() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            with(cookie) {
                httpOnly = true
//                secure = true todo: use ssl
            }
        }
    }
}

data class UserSession(val sessionId: String)
