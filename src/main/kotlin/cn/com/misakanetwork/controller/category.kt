package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.ArticleCategoryDTO
import cn.com.misakanetwork.service.CategoryService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun categoryController(app: Application) {
    app.routing {
        route("/category/article/") {
            get {
                CategoryService(call).getArticle()
            }
            post {
                val dto: ArticleCategoryDTO
                try {
                    dto = call.receive<ArticleCategoryDTO>()
                } catch (E: Throwable) {
                    throw BadRequestException("请求体不合法")
                }
                CategoryService(call).addArticleCategory(dto)
            }
        }
        get("/category/article/sum") {
            CategoryService(call).getArticleSum()
        }
    }
}
