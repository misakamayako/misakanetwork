package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dao.CategoryDAO
import cn.com.misakanetwork.dto.CategoryDTO
import cn.com.misakanetwork.dto.CategorySumDTO
import cn.com.misakanetwork.plugins.InternalServerError
import cn.com.misakanetwork.plugins.database
import cn.com.misakanetwork.vo.CategoryVo
import io.ktor.server.plugins.*
import org.ktorm.dsl.*

class CategoryService : CategoryVo {
	override suspend fun getCategory(type: Int?): List<CategoryDTO> {
		return database.from(CategoryDAO).select().where { CategoryDAO.type eq (type ?: 1) }.map {
			CategoryDTO(it[CategoryDAO.description], it[CategoryDAO.id], it[CategoryDAO.type])
		}
	}

	override suspend fun addCategory(categoryDTO: CategoryDTO): CategoryDTO {
		try {
			database.insert(CategoryDAO) {
				set(CategoryDAO.description, categoryDTO.category)
				set(CategoryDAO.type, categoryDTO.type ?: 1)
			}
		} catch (e: Error) {
			throw BadRequestException("类型名称重复")
		}
		return database.from(CategoryDAO).select().where { CategoryDAO.description eq categoryDTO.category!! }.map {
			CategoryDTO(
				it[CategoryDAO.description],
				it[CategoryDAO.id],
				it[CategoryDAO.type]
			)
		}.getOrElse(0) {
			throw InternalServerError("插入失败")
		}
	}

	override suspend fun getArticleCategorySum(type: Int?): List<CategorySumDTO> {
		return database
			.from(ArticleToCategoryDAO)
			.innerJoin(CategoryDAO, CategoryDAO.id eq ArticleToCategoryDAO.category)
			.select(CategoryDAO.description, CategoryDAO.id, CategoryDAO.type, count().aliased("count"))
			.groupBy(CategoryDAO.description)
			.orderBy(count().aliased("count").desc()).map {
				CategorySumDTO(
					it[CategoryDAO.description],
					it[CategoryDAO.id],
					it[CategoryDAO.type],
					it.getInt("count")
				)
			}
	}
}
