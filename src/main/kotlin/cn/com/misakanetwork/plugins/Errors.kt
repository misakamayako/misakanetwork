package cn.com.misakanetwork.plugins

class AuthenticationException : RuntimeException()
class AuthorizationException(override val message:String="") : RuntimeException()
class UnauthorizedException : RuntimeException()
