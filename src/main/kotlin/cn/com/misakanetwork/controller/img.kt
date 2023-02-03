package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.AlbumQueryDTO
import cn.com.misakanetwork.dto.AlbumViewSetting
import cn.com.misakanetwork.dto.CreateAlbumDTO
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.redisPool
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.ImgService
import cn.com.misakanetwork.tools.PasswordEncryption.generateSalt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun imgController(app: Application) {
	val imgService by lazy { ImgService() }
	app.routing {
		route("/album") {
			post("/album") {
				requireLogin {
					val createAlbumDTO = call.receive<CreateAlbumDTO>()
					call.respond(ResponseDTO(data = imgService.createAlbum(createAlbumDTO)))
				}
			}
			get("/albumList") {
				val resource = redisPool.resource
				val setting = if (call.request.cookies["imgSetting"].isNullOrEmpty()) {
					resource[call.request.cookies["imgSetting"]].toInt()
				} else {
					0b000
				}
				val query = call.request.queryParameters.let {
					AlbumQueryDTO(
						it["page"]?.toInt(),
						it["pageSize"]?.toInt(),
						it["keyword"],
						it["category"]?.toInt(),
						(0b001 and setting) == 0b001,
						(0b010 and setting) == 0b010,
					)
				}
				call.respond(ResponseDTO(data = imgService.getAlbumList(query)))
			}
			patch("/viewSetting") {
				val albumViewSetting = call.receive<AlbumViewSetting>()
				val resource = redisPool.resource
				var id: String
				var setting = if (call.request.cookies["imgSetting"].isNullOrEmpty()) {
					val cookie = Cookie(
						name = "imgSetting",
						value = generateSalt().also { id = it },
						maxAge = 3600,
						httpOnly = true,
						secure = true,
						path = "/lastOrder/album"
					)
					call.response.cookies.append(cookie)
					0b000
				} else {
					resource[call.request.cookies["imgSetting"]!!.also { id = it }].toInt()
				}
				if (albumViewSetting.nsfw != null) {
					setting = if (albumViewSetting.nsfw) {
						setting or 0b001
					} else {
						setting xor 0b001
					}
				}
				if (albumViewSetting.private != null) {
					setting = if (albumViewSetting.private) {
						setting or 0b010
					} else {
						setting xor 0b010
					}
				}
				resource.setex(id, 3600L, setting.toString())
				call.respond(ResponseDTO(data = "ok"))
			}
		}
	}
}
