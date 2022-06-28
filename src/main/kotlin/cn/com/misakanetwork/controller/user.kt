package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.LoginDTO
import cn.com.misakanetwork.service.user.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun userController(app: Application) {
    app.routing {
        post<LoginDTO>("/login") {
            UserService(call).login(it)
        }
        put<LoginDTO>("/login") {
            UserService(call).put(it)
        }
        get("/user/info") {
            UserService(call).getUserInfo()
        }
    }
}
