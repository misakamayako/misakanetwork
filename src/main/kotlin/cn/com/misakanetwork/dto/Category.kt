package cn.com.misakanetwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategorySumDTO(val category: String?, val id: Int?, val count: Int?)

@Serializable
data class ArticleCategoryDTO(val category: String? = null, val id: Int? = null)
