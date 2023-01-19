package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.ArticleService
import cn.com.misakanetwork.vo.ArticleUploadVO
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun articleController(app: Application) {
	val articleService by lazy {
		println("ArticleService init")
		ArticleService()
	}
	app.routing {
		route("/article") {
			get("/list") {
				call.respond(
					ResponseDTO(
						data = articleService.getArticleList(
							call.request.queryParameters["page"]?.toInt() ?: 1,
							call.request.queryParameters["pageSize"]?.toInt() ?: 10,
							call.request.queryParameters["category"]?.toInt(),
						)
					)
				)
			}
			get("/all") {
				call.respond(ResponseDTO(data = articleService.getAllArticleList()))
			}
			post("/upload") {
				requireLogin {
					val formPart = call.receive<ArticleUploadVO>()
					call.respond(ResponseDTO(data = articleService.uploadArticle(formPart)))
				}
			}
			get("{id}") {
				val id = call.parameters["id"]?.toIntOrNull()
				if (id == null) {
					throw BadRequestException("参数必须为int类型")
				} else {
					call.respond(ResponseDTO(data = articleService.getArticle(id)))
				}
			}
		}
	}
}
