package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable
import java.rmi.ServerException

@Serializable
data class Response<T>(val status: Int = 200, val data: T?)

@Serializable
data class Pagination<T : List<*>>(val page: Int, val pageSize: Int, val data: T) {
    init {
        if (pageSize < data.size) {
            throw ServerException("数据量与页面大小不匹配")
        }
    }
}

