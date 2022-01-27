package cn.com.misakanetwork.plugins

class AuthenticationException : RuntimeException()
class AuthorizationException(s: String?=null) :RuntimeException(s)
class NotAcceptableException(s: String?=null) :RuntimeException(s)
