package cn.com.misakanetwork.plugins

abstract class ServerErrors(val s: String? = null) : RuntimeException(s) {
	abstract val httpErrorCode: Int
}

class AuthenticationException : ServerErrors("身份认证失败") {
	override val httpErrorCode: Int = 401
}

class AuthorizationException(s: String?) : ServerErrors(s) {
	override val httpErrorCode: Int = 401
}

class NotAcceptableException(s: String?) : ServerErrors(s) {
	override val httpErrorCode: Int = 406
}

class InternalServerError(s: String?) : ServerErrors(s) {
	override val httpErrorCode: Int = 500
}
