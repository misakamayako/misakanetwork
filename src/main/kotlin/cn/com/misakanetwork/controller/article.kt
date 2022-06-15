package cn.com.misakanetwork.controller

import cn.com.misakanetwork.service.ArticleService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun articleController(app: Application) {
    app.routing {
        route("/article") {
            get("/list") {
                ArticleService(call).getArticleList(call.request.queryParameters["page"]?.toInt() ?: 1)
            }
            post("/upload"){
                ArticleService(call).upload()
            }
            get("{id}"){
                val id = call.parameters["id"]?.toIntOrNull()
                if(id==null){
                    call.respondRedirect("/article/list")
                } else {
                    ArticleService(call).getArticle(id)
                }
            }
        }
    }
}
