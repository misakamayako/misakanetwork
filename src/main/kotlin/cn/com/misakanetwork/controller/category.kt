package cn.com.misakanetwork.controller

import cn.com.misakanetwork.service.article.CategoryService
import io.ktor.application.*
import io.ktor.routing.*

fun categoryController(app:Application){
    app.routing {
        get("/category/article"){
            CategoryService(call).getArticle()
        }
    }
}
