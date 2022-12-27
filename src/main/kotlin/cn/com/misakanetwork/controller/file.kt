package cn.com.misakanetwork.controller

import cn.com.misakanetwork.dto.ResponseDTO
import cn.com.misakanetwork.plugins.InternalServerError
import cn.com.misakanetwork.plugins.requireLogin
import cn.com.misakanetwork.service.FileService
import com.aliyun.oss.OSSException
import io.ktor.http.content.*
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
                content.forEachPart {
                    if (it is PartData.FileItem) {
                        try {
                            val tail = it.originalFileName?.substringAfterLast('.')
                            call.respond(
                                ResponseDTO(
                                    data = fileService
                                        .fileUpload(
                                            withContext(Dispatchers.IO) {
                                                it.streamProvider().readAllBytes()
                                            },
                                            tail
                                        )
                                )
                            )
                        } catch (e: OSSException) {
                            throw InternalServerError("oss失败" + e.message)
                        } catch (e: Exception) {
                            throw InternalServerError(e.message)
                        }
                    }
                }
                if (!call.response.isCommitted) {
                    throw InternalServerError("未知错误")
                }
            }
        }
    }
}
