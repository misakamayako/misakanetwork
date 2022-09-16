package cn.com.misakanetwork.controller

import cn.com.misakanetwork.service.ArticleService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun articleController(app: Application) {
    app.routing {
        route("/article") {
            get("/list") {
                ArticleService(call).getArticleList(call.request.queryParameters["page"]?.toInt() ?: 1)
            }
            get("/all") {
                ArticleService(call).getAllArticle()
            }
            post("/upload") {
                ArticleService(call).upload()
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    throw BadRequestException("参数必须为int类型")
                } else {
                    ArticleService(call).getArticle(id)
                }
            }
        }
    }
}
