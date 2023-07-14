package cn.com.misakanetwork.tools

import cn.com.misakanetwork.dto.ErrorDetail
import cn.com.misakanetwork.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

suspend fun notFindHandle(call: ApplicationCall) {
    if (call.request.accept()?.contains("json") == true) {
        call.respond(
            HttpStatusCode.NotFound,
            ErrorResponse(ErrorDetail(404, "The requested resource is not found", call.request.uri))
        )
    } else {
        call.respondRedirect("/404")
    }
}

suspend fun unauthorizedHandle(call: ApplicationCall) {
    if (call.request.accept()?.contains("json") == true) {
        call.respond(
            HttpStatusCode.Unauthorized,
            ErrorResponse(
                ErrorDetail(
                    HttpStatusCode.Unauthorized.value,
                    "You are not authorized to access this resource. Please log in and try again.",
                    call.request.uri
                )
            )
        )
    } else {
        call.respondRedirect("/401")
    }
}

suspend fun badRequestHandle(call: ApplicationCall, required: String) {
    if (call.request.accept()?.contains("json") == true) {
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse(
                ErrorDetail(
                    HttpStatusCode.BadRequest.value,
                    "The request is missing a required parameter '$required'",
                    call.request.uri
                )
            )
        )
    } else {
        call.respondRedirect("/400")
    }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any> badRequestHandle(call: ApplicationCall) {
    val type = T::class.serializer().descriptor
    println(type)
    if (call.request.accept()?.contains("json") == true) {
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse(
                ErrorDetail(
                    HttpStatusCode.BadRequest.value,
                    "The request is missing a required parameter '${type}'",
                    call.request.uri
                )
            )
        )
    } else {
        call.respondRedirect("/400")
    }
}
