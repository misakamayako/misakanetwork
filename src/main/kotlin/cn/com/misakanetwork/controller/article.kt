package cn.com.misakanetwork.controller

import cn.com.misakanetwork.service.ArticleService
import cn.com.misakanetwork.service.article.ArticleReader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun articleController(app: Application) {
    app.routing {
        route("/article") {
            get("/brief") {
                ArticleService(call).getBrief(call.request.queryParameters["page"]?.toInt() ?: 1)
            }
            post("/upload"){
                ArticleService(call).upload()
            }
        }
        get("/article") {
            throw NotFoundException()
        }

//        authenticate("auth-jwt") {
        get("/article/upload") {
            call.respondFile(File("src/resources/html/articleUpload.html"))
        }
//        post<NewArticle>("/article/upload") {
//
//        }
//        }
        // get article by id
        get("/article/{id}") {
            val id = call.parameters["id"] ?: throw BadRequestException("request url is not valid")
            val instance = ArticleReader(call)
            instance.getOrRenderFile(id.toIntOrNull())
        }
    }
}

//data class NewArticle(val title: String, val content: String, val tags: Array<String>)
