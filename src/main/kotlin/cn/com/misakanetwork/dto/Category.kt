package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategorySumDTO(val category: String?, val id: Int?, val type: Int?, val count: Int?)

@Serializable
data class CategoryDTO(val category: String? = null, val id: Int? = null, val type: Int?)
