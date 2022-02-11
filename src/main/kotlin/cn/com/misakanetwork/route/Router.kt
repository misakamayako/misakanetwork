package cn.com.misakanetwork.route

import cn.com.misakanetwork.controller.articleController
import cn.com.misakanetwork.controller.imgController
import cn.com.misakanetwork.controller.userController
import cn.com.misakanetwork.controller.categoryController
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.router() {
    routing {
        static("assets/img") {
            resources("assets/img")
//            resources("js")
        }
    }
    articleController(this)
    imgController(this)
    userController(this)
    categoryController(this)
}
