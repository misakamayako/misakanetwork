package cn.com.misakanetwork.vo

import cn.com.misakanetwork.dto.ArticleAllDTO
import cn.com.misakanetwork.dto.ArticleDTO
import cn.com.misakanetwork.dto.ArticleDetailDTO
import cn.com.misakanetwork.dto.PaginationDTO
import cn.com.misakanetwork.tools.NotBlank
import kotlinx.serialization.Serializable
import org.ktorm.dsl.Query

interface ArticleVO {
	fun getDefaultQuery(): Query
	fun getByCategory(): Query
	fun getResult(query: Query): List<ArticleDTO>
	suspend fun getArticleList(page: Int, pageSize: Int, category: Int?): PaginationDTO<List<ArticleDTO>>
	suspend fun uploadArticle(articleUploadVO: ArticleUploadVO): ArticleDTO
	suspend fun getArticle(id: Int): ArticleDetailDTO
	suspend fun getAllArticleList(): List<ArticleAllDTO>
}

@Serializable
data class ArticleUploadVO(
	@NotBlank
	val title: String,
	val categories: List<Int>,
	val content: String
)
