package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.LoginDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.getToken
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.plugins.setToken
import cn.com.misakanetwork.service.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun userController(app: Application) {
    val resource by lazy { UserService() }
    app.routing {
        post<LoginDTO>("/login") {
            setToken(call, resource.login(it))
            call.respond(ResponseDTO(data = "ok"))
        }
        put<LoginDTO>("/login") {
            call.respond(resource.put(it))
        }
        get("/user/info") {
            requireLogin {
                call.respond(resource.getUserInfo(getToken(call)!!))
            }
        }
    }
}
