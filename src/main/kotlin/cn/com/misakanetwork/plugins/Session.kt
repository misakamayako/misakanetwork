package cn.com.misakanetwork.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import java.util.*

suspend fun <TSubject : Any> PipelineContext<TSubject, ApplicationCall>.requireLogin(
	function: suspend () -> TSubject
): TSubject {
	val cookie = context.request.cookies["csrfToken"]
	if (cookie is String) {
		val resource = redisPool.resource
		if (resource[cookie] != "") {
			resource.close()
			return function()
		} else {
			throw AuthorizationException("用户未授权")
		}
	} else {
		throw AuthorizationException("用户未授权")
	}
}

fun setToken(call: ApplicationCall, token: String) {
	val cookie = Cookie(
		name = "__Secure-csrfToken",
		value = token,
		maxAge = 7 * 24 * 3600,
		httpOnly = true,
		secure = true,
		path = "/lastOrder"
	)
	val resource = redisPool.resource
	resource.setex(token, 7 * 24 * 3600L, Date().toString())
	call.response.cookies.append(cookie)
}

fun getToken(call: ApplicationCall): String? {
	return call.request.cookies["__Secure-csrfToken"]
}
