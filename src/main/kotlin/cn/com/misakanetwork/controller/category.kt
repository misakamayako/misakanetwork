package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.CategoryDTO
import cn.com.misakanetwork.dto.ErrorDetail
import cn.com.misakanetwork.dto.ErrorResponse
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.CategoryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun categoryController(app: Application) {
    app.routing {
        val categoryService by lazy { CategoryService() }
        route("/category") {
            get {
                val type = call.request.queryParameters["type"]?.toInt()
                val keyword = call.request.queryParameters["keyword"]
                call.respond(ResponseDTO(data = categoryService.getCategory(type, keyword)))
            }
            post {
                requireLogin {
                    val dto: CategoryDTO
                    try {
                        dto = call.receive<CategoryDTO>()
                    } catch (e: Throwable) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse(ErrorDetail(400, e.message ?: "未知错误", call.request.path()))
                        )
                        return@requireLogin
                    }
                    call.respond(HttpStatusCode.Created, ResponseDTO(data = categoryService.addCategory(dto)))
                }
            }
        }
        get("/category/article/sum") {
            val type: Int? = call.request.queryParameters["type"]?.toInt()
            call.respond(ResponseDTO(data = categoryService.getArticleCategorySum(type)))
        }
    }
}
