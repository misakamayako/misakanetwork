package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.dto.ResponseDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPages() {
	install(StatusPages) {
		statusFile(HttpStatusCode.NotFound, HttpStatusCode.Unauthorized, filePattern = "error#.html")

		exception<ServerErrors> { call: ApplicationCall, exception: ServerErrors ->
			ExceptionHandler(exception).response(call)
		}
		exception<Error> { call: ApplicationCall, exception: Error ->
			call.respond(status = HttpStatusCode.InternalServerError, exception.stackTrace)
		}
	}
}

class ExceptionHandler(private val exception: ServerErrors) {
	private fun acceptJson(call: ApplicationCall): Boolean =
		call.request.headers.getAll("Accept")?.find { it.contains("json") } != null

	suspend fun response(call: ApplicationCall) {
		if (acceptJson(call)) {
			val response = ResponseDTO<String>(
				status = exception.httpErrorCode.value,
				data = null,
				message = exception.s ?: exception.httpErrorCode.description
			)
			call.respond(status = exception.httpErrorCode, response)
		} else {
			call.respond(status = exception.httpErrorCode, exception.s ?: exception.httpErrorCode.description)
		}
	}
}
