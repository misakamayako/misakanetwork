package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.dto.ResponseDTO
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import java.util.*

fun Application.statusPages() {
    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, HttpStatusCode.Unauthorized, filePattern = "error#.html")
        exception<AuthenticationException> { cause ->
            val accept = call.request.headers["accept"] ?: return@exception call.respond(HttpStatusCode.Unauthorized,
                                                                                         cause.message ?: "")
            if (accept.lowercase(Locale.getDefault()).contains("json")) {
                call.respond(HttpStatusCode.Unauthorized, back(HttpStatusCode.Unauthorized, cause))
            } else {
                call.respond(HttpStatusCode.Unauthorized, cause.message ?: "")
            }
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<NotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<Exception> { cause ->
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

fun back(httpStatusCode: HttpStatusCode, error: Exception) =
    ResponseDTO<String>(
        status = httpStatusCode.value,
        data = null,
        message = error.message ?: httpStatusCode.description
    )
