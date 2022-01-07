package cn.com.misakanetwork.controller

import cn.com.misakanetwork.service.article.ArticleReader
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun article(app: Application) {
    app.routing {
        get("/article") {
            throw NotFoundException()
        }
        authenticate("auth-jwt") {
            get("/article/upload") {
                call.respondFile(File("src/resources/html/articleUpload.html"))
            }
            post<NewArticle>("/article/upload"){

            }
        }
        // get article by id
        get("/article/{id}") {
            val id = call.parameters["id"] ?: throw BadRequestException("request url is not valid")
            val instance = ArticleReader(call)
            instance.getOrRenderFile(id.toIntOrNull())
        }
    }
}
data class NewArticle(val title:String, val content:String, val tags:Array<String>)
