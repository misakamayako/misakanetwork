package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
	val error: ErrorDetail
)

@Serializable
data class ErrorDetail(
	val code: Int,
	val message: String,
	val requestPath: String
)
