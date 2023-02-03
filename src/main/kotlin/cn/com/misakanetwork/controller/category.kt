package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.CategoryDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.CategoryService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun categoryController(app: Application) {
	app.routing {
		val categoryService by lazy { CategoryService() }
		route("/category/categories/") {
			get {
				val type: Int? = call.request.queryParameters["type"]?.toInt()
				call.respond(ResponseDTO(data = categoryService.getCategory(type)))
			}
			post {
				requireLogin {
					val dto: CategoryDTO
					try {
						dto = call.receive()
					} catch (e: Throwable) {
						throw BadRequestException("请求体不合法")
					}
					call.respond(ResponseDTO(data = categoryService.addCategory(dto)))
				}
			}
		}
		get("/category/article/sum") {
			val type: Int? = call.request.queryParameters["type"]?.toInt()
			call.respond(ResponseDTO(data = categoryService.getArticleCategorySum(type)))
		}
	}
}
