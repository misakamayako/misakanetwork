package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.sessions.*

//public fun Route.checkSession(
//    vararg configurations: String? = arrayOf<String?>(null),
//    optional: Boolean = false,
//    build: Route.() -> Unit
//): Route {
//    val authenticatedRoute = createChild()
//    authenticatedRoute.build()
//    return authenticatedRoute
//}
fun Application.session(){
    install(Sessions) {
        cookie<UserSession>("user_session"){
            with(cookie){
                httpOnly = true
//                secure = true todo: use ssl
            }
        }
    }
}

data class UserSession(val sessionId:String)
