package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.CategoryDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dto.CategoryDTO
import cn.com.misakanetwork.dto.CategorySumDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.database
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import java.sql.SQLIntegrityConstraintViolationException

class CategoryService(private val call: ApplicationCall) {
	suspend fun getArticleSum() {
		val query = database
			.from(ArticleToCategoryDAO)
			.innerJoin(CategoryDAO, CategoryDAO.id eq ArticleToCategoryDAO.category)
			.select(CategoryDAO.description, CategoryDAO.id, count().aliased("count"))
			.groupBy(CategoryDAO.description)
			.orderBy(count().aliased("count").desc())
		val result = ArrayList<CategorySumDTO>()
		for (q in query) {
			result.add(
				CategorySumDTO(
					q[CategoryDAO.description],
					q[CategoryDAO.id],
					q[count().aliased("count")]
				)
			)
		}
		call.respond(ResponseDTO(data = result))
	}

	suspend fun getArticle() {
		val query = database.from(CategoryDAO).select()
		val result = ArrayList<CategoryDTO>()
		for (row in query) {
			result.add(CategoryDTO(row[CategoryDAO.description], row[CategoryDAO.id]))
		}
		call.respond(ResponseDTO(data = result))
	}

	suspend fun addArticleCategory(categoryDTO: CategoryDTO) {
		if (categoryDTO.category == null) {
			throw BadRequestException("exception")
		}
		try {
			database.insert(CategoryDAO) {
				set(CategoryDAO.description, categoryDTO.category)
			}
		} catch (e: SQLIntegrityConstraintViolationException) {
			throw BadRequestException("类型名称重复")
		}
		val result = database
			.from(CategoryDAO)
			.select()
			.where {
				CategoryDAO.description eq categoryDTO.category
			}
			.map {
				CategoryDTO(it[CategoryDAO.description], it[CategoryDAO.id])
			}
		call.respond(ResponseDTO(data = result[0]))
	}
}
