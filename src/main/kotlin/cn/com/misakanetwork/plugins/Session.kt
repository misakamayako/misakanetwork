package cn.com.misakanetwork.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
suspend fun <TSubject : Any> PipelineContext<TSubject, ApplicationCall>.requireLogin(function: suspend () -> TSubject): TSubject {
    val cookie = context.request.cookies["csrfToken"]
    if (cookie is String) {
        // TODO 添加身份认证与redis
        return function()
    } else {
        throw AuthorizationException("用户未授权")
    }
}

fun setToken(call: ApplicationCall, token: String) {
    val cookie = Cookie(
        name = "csrfToken",
        value = token,
        maxAge = 7 * 24 * 3600,
        httpOnly = true,
        secure = false,
        path = "/lastOrder"
    )
    call.response.cookies.append(cookie);
}

fun getToken(call: ApplicationCall): String? {
    return call.request.cookies["csrfToken"]
}
