package cn.com.misakanetwork.route

import cn.com.misakanetwork.controller.*
import io.ktor.server.application.*

fun Application.router() {
    articleController(this)
    imgController(this)
    userController(this)
    categoryController(this)
    fileController(this)
}
