package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.dto.ResponseDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPages() {
    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, HttpStatusCode.Unauthorized, filePattern = "error#.html")

        exception<Exception>{ call: ApplicationCall, exception: Exception ->
            ExceptionHandler(exception).response(call)
        }
    }
}

class ExceptionHandler(private val exception: Exception) {
    private fun acceptJson(call: ApplicationCall): Boolean =
        call.request.headers.getAll("Accept")?.find { it.contains("json") } != null

    suspend fun response(call: ApplicationCall) {
        val statusCode = when (exception) {
            is AuthenticationException -> HttpStatusCode.Unauthorized
            is AuthorizationException -> HttpStatusCode.Unauthorized
            is NotFoundException -> HttpStatusCode.NotFound
            is BadRequestException -> HttpStatusCode.BadRequest
            else -> HttpStatusCode.InternalServerError
        }
        if (acceptJson(call)) {
            val response = ResponseDTO<String>(
                status = statusCode.value,
                data = null,
                message = exception.message.takeIf { it.isNullOrEmpty() } ?: statusCode.description
            )
            call.respond(statusCode, response)
        } else {
            call.respond(statusCode, exception.message.takeIf { it.isNullOrEmpty() } ?: statusCode.description)
        }
    }
}
