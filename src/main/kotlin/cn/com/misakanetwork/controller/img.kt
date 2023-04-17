package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.*
import cn.com.misakanetwork.plugins.checkLogin
import cn.com.misakanetwork.plugins.getToken
import cn.com.misakanetwork.plugins.redisPool
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.ImgService
import cn.com.misakanetwork.tools.badRequestHandle
import cn.com.misakanetwork.tools.notFindHandle
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun imgController(app: Application) {
	val imgService by lazy { ImgService() }
	app.routing {
		route("/album") {
			post {
				requireLogin {
					val createAlbumDTO = call.receive<CreateAlbumDTO>()
					call.respond(ResponseDTO(data = imgService.createAlbum(createAlbumDTO)))
				}
			}
			//获取相册列表
			get {
				val setting = (call.request.cookies["imgSetting"] ?: "0").toInt()
				val query = call.request.queryParameters.let {
					AlbumQueryDTO(
						it["page"]?.toInt()?:1,
						it["pageSize"]?.toInt()?:20,
						it["keyword"],
						it["category"]?.toInt(),
						(0b001 and setting) == 0b001,
						(0b010 and setting) == 0b010,
					)
				}
				call.respond(imgService.getAlbumList(query))
			}
			//获取指定相册
			get("{id}") {
				val id = call.parameters["id"]?.toIntOrNull()
				if (id == null) {
					notFindHandle(call)
					return@get
				}
				call.respond(ResponseDTO(data = imgService.getAlbum(id)))
			}
			//删除指定相册
			delete("{id}") {
				requireLogin {
					val id = call.parameters["id"]?.toIntOrNull()
					if (id == null) {
						badRequestHandle(call, "album's id")
						return@requireLogin
					}
					imgService.deleteAlbum(id)
					call.respondBytes(bytes = byteArrayOf(), status = HttpStatusCode.NoContent)
				}
			}
		}
		route("/images") {
			post {
				requireLogin {
					val imageInfo = try {
						call.receive<ImgUploadDTO>()
					} catch (_: Exception) {
						badRequestHandle<ImgUploadDTO>(call)
						return@requireLogin
					}
					call.respond(ResponseDTO(data = imgService.uploadImg(imageInfo)))
				}
			}
			get {
				val cookie = context.request.cookies["__Secure-csrfToken"]
				val showPrivate = if (cookie is String) {
					val resource = redisPool.resource
					(resource[cookie] != "" && context.request.cookies["__Secure-Private"] == "T").also { resource.close() }
				} else {
					false
				}
				val showNSFW = context.request.cookies["__Secure-NSFW"] == "T"
				val page = (call.request.queryParameters["page"] ?: "1").toInt()
				val pageSize = (call.request.queryParameters["pageSize"] ?: "20").toInt()
				val tags = (call.request.queryParameters["tags"])?.split(",")?.map(String::toInt)
				call.respond(ResponseDTO(data = imgService.getImgList(page, pageSize, tags, showPrivate, showNSFW)))
			}
			get("{eigenvalues}") {
				TODO("获取指定图片")
			}
			delete("{eigenvalues}") {
				requireLogin {
					TODO("删除图片")
				}
			}
			put("{eigenvalues}") {
				requireLogin {
					TODO("更改图片配置和访问控制")
				}
			}
			patch("setting") {
				val albumViewSetting = call.receive<AlbumViewSetting>()
				call.response.cookies.append(
					Cookie(
						name = "__Secure-NSFW",
						value = if (albumViewSetting.nsfw == true) "T" else "F",
						maxAge = 3600,
						secure = true,
						httpOnly = true
					)
				)
				if (checkLogin(getToken(call))) {
					call.response.cookies.append(
						Cookie(
							name = "__Secure-Private",
							value = if (albumViewSetting.private == true) "T" else "F",
							maxAge = 1800,
							secure = true,
							httpOnly = true
						)
					)
				}
				call.respondBytes(bytes = byteArrayOf(), status = HttpStatusCode.NoContent)
			}
		}
	}
}
