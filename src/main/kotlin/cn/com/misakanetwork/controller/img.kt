package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.CreateAlbumDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.ImgService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun imgController(app: Application) {
	val imgService by lazy { ImgService() }
	app.routing {
		route("/img") {
			post("/album") {
				requireLogin {
					val createAlbumDTO = call.receive<CreateAlbumDTO>()
					call.respond(ResponseDTO(data = imgService.createAlbum(createAlbumDTO)))
				}
			}
		}
	}
}
