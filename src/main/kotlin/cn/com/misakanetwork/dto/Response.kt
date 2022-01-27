package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable
import java.rmi.ServerException

@Serializable
data class Response<out T>(val status: Int = 200, val data: T?, val message: String = "ok")

@Serializable
data class Pagination<out T : List<*>>(val page: Int, val pageSize: Int, val data: T) {
    init {
        if (pageSize < data.size) {
            throw ServerException("数据量与页面大小不匹配")
        }
    }
}

