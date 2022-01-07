package cn.com.misakanetwork.route

import cn.com.misakanetwork.controller.article
import cn.com.misakanetwork.controller.imgController
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.router() {
    routing {
        static("assets") {
            resources("img")
            resources("js")
        }
    }
    article(this)
    imgController(this)
}
