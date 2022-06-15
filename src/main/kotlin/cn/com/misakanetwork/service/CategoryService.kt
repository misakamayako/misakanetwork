package cn.com.misakanetwork.service

import cn.com.misakanetwork.dao.ArticleCategoryDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dto.ArticleCategoryDTO
import cn.com.misakanetwork.dto.CategorySumDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import org.ktorm.dsl.*
import java.sql.SQLIntegrityConstraintViolationException

class CategoryService(private val call: ApplicationCall) {
    suspend fun getArticleSum() {
        val query = database
            .from(ArticleToCategoryDAO)
            .innerJoin(ArticleCategoryDAO, ArticleCategoryDAO.id eq ArticleToCategoryDAO.category)
            .select(ArticleCategoryDAO.description, ArticleCategoryDAO.id, count().aliased("count"))
            .groupBy(ArticleCategoryDAO.description)
        val result = ArrayList<CategorySumDTO>()
        for (q in query) {
            result.add(CategorySumDTO(q[ArticleCategoryDAO.description],
                                      q[ArticleCategoryDAO.id],
                                      q[count().aliased("count")]))
        }
        call.respond(ResponseDTO(data = result))
    }

    suspend fun getArticle() {
        val query = database.from(ArticleCategoryDAO).select()
        val result = ArrayList<ArticleCategoryDTO>()
        for (row in query) {
            result.add(ArticleCategoryDTO( row[ArticleCategoryDAO.description],row[ArticleCategoryDAO.id]))
        }
        call.respond(ResponseDTO(data = result))
    }

    suspend fun addArticleCategory(articleCategoryDTO: ArticleCategoryDTO) {
        if (articleCategoryDTO.category == null) {
            throw BadRequestException("类型名称为必传项")
        }
        try{
            database.insert(ArticleCategoryDAO) {
                set(ArticleCategoryDAO.description, articleCategoryDTO.category)
            }
        } catch (E: SQLIntegrityConstraintViolationException){
            throw BadRequestException("类型名称重复")
        }
        val result = database
            .from(ArticleCategoryDAO)
            .select()
            .where {
                ArticleCategoryDAO.description eq articleCategoryDTO.category
            }
            .map {
                ArticleCategoryDTO(it[ArticleCategoryDAO.description], it[ArticleCategoryDAO.id])
            }
        call.respond(ResponseDTO(data = result[0]))
    }
}
