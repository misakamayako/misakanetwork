package cn.com.misakanetwork.service

import cn.com.misakanetwork.dto.ResponseDTO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

open class Service(val call: ApplicationCall) {
    suspend fun <T> response(status: HttpStatusCode, data: T) {
        call.respond(status, ResponseDTO<T>(status = 200, data = data, message = status.description))
    }

    suspend fun  response(data: Any) {
        response(HttpStatusCode.OK, data)
    }
}
