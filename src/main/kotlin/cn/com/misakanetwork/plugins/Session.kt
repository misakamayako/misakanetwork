package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.tools.unauthorizedHandle
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import java.util.*

suspend fun <TSubject : Any> PipelineContext<TSubject, ApplicationCall>.requireLogin(
    next: suspend () -> TSubject,
) {
    if (checkLogin(getToken(context))) {
        next()
    } else {
        unauthorizedHandle(context)
    }
}

fun setToken(call: ApplicationCall, token: String) {
    val cookie = Cookie(
        name = "__Secure-csrfToken",
        value = token,
        maxAge = 7 * 24 * 3600,
        httpOnly = true,
        secure = true,
        path = "/"
    )
    val resource = redisPool.resource
    resource.setex(token, 7 * 24 * 3600L, Date().toString())
    call.response.cookies.append(cookie)
}

fun getToken(call: ApplicationCall): String? {
    return call.request.cookies["__Secure-csrfToken"]
}

fun checkLogin(token: String?): Boolean {
    return if (token is String) {
        val resource = redisPool.resource
		val flag = resource[token] != ""
		resource.close()
		flag
    } else {
        false
    }
}
