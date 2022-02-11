package cn.com.misakanetwork.service.article

import cn.com.misakanetwork.dao.ArticleCategoryDAO
import cn.com.misakanetwork.dao.ArticleToCategoryDAO
import cn.com.misakanetwork.dto.CategorySumDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.database
import io.ktor.application.*
import io.ktor.response.*
import org.ktorm.dsl.*

class CategoryService(private val call: ApplicationCall) {
    suspend fun getArticle() {
        val query = database
            .from(ArticleToCategoryDAO)
            .innerJoin(ArticleCategoryDAO, ArticleCategoryDAO.id eq ArticleToCategoryDAO.category)
            .select(ArticleCategoryDAO.description,ArticleCategoryDAO.id, count().aliased("count"))
            .groupBy(ArticleCategoryDAO.description)
        val result = ArrayList<CategorySumDTO>()
        for (q in query) {
            result.add(CategorySumDTO(q[ArticleCategoryDAO.description], q[ArticleCategoryDAO.id], q[count().aliased("count")]))
        }
        call.respond(ResponseDTO(data = result))
    }
}
