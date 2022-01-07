package cn.com.misakanetwork.dao

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(val status: Int, val message: T?)

@Serializable
data class Pagination<T : List<*>>(val data: T)

