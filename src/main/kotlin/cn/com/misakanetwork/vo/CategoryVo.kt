package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.CategoryDTO
import cn.com.misakanetwork.dto.CategorySumDTO

interface CategoryVo {
	suspend fun getCategory(type: Int?): List<CategoryDTO>
	suspend fun addCategory(categoryDTO: CategoryDTO): CategoryDTO
	suspend fun getArticleCategorySum(type: Int? = 1): List<CategorySumDTO>
}
