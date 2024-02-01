package cn.com.misakanetwork.plugins

import io.ktor.http.*

abstract class ServerErrors(val s: String? = null) : RuntimeException(s) {
	abstract val httpErrorCode: HttpStatusCode
}

class AuthenticationException : ServerErrors("身份认证失败") {
	override val httpErrorCode: HttpStatusCode = HttpStatusCode.Unauthorized
}

class AuthorizationException(s: String?) : ServerErrors(s) {
	override val httpErrorCode: HttpStatusCode = HttpStatusCode.Unauthorized
}

class NotAcceptableException(s: String?) : ServerErrors(s) {
	override val httpErrorCode: HttpStatusCode = HttpStatusCode.MethodNotAllowed
}

class InternalServerError(s: String?) : ServerErrors(s) {
	override val httpErrorCode: HttpStatusCode = HttpStatusCode.InternalServerError
}

class UnavailableForLegalReasons(s: String? = null) : ServerErrors(s) {
	override val httpErrorCode: HttpStatusCode = HttpStatusCode(451, "Unavailable For Legal Reasons")
}
