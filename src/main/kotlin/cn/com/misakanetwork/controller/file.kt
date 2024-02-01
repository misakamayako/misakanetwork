package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.ErrorDetail
import cn.com.misakanetwork.dto.ErrorResponse
import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.enum.OSSInfo
import cn.com.misakanetwork.plugins.InternalServerError
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.FileService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun fileController(app: Application) {
    val fileService by lazy { FileService() }
    app.routing {
        post("/file/upload") {
            requireLogin {
                val content = call.receiveMultipart()
                var fileBytes: ByteArray? = null
                var tail: String? = null
                var bucket: OSSInfo = OSSInfo.MARKDOWN
                content.forEachPart {
                    when (it) {
                        is PartData.FileItem -> {
                            try {
                                tail = it.originalFileName?.substringAfterLast('.')
                                fileBytes = withContext(Dispatchers.IO) {
                                    it.streamProvider().readAllBytes()
                                }
                            } catch (e: Exception) {
                                throw InternalServerError(e.message)
                            }
                        }

                        is PartData.FormItem -> {
							when(it.name){
								"type"->{
									bucket = if (it.value == "PICTURE") OSSInfo.PICTURE else OSSInfo.OpenSource
								}
							}
                        }

                        else -> {
                        }
                    }
                }
                if (fileBytes != null) {
                    call.respond(
                        HttpStatusCode.Created,
                        ResponseDTO(data = fileService.fileUpload(fileBytes!!, bucket, tail))
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(ErrorDetail(400, "上传文件不能为空", call.request.uri))
                    )
                }
            }
        }
    }
}
