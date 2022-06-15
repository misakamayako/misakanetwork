package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDTO<out T>(val status: Int = 200, val data: T?, val message: String = "ok")

@Serializable
data class PaginationDTO<out T : List<*>>(val total: Int, val page: Int, val pageSize: Int, val data: T)
