package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.statusPages(){
    install(StatusPages) {
        statusFile(HttpStatusCode.NotFound, HttpStatusCode.Unauthorized, filePattern = "error#.html")
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<NotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<Exception>{cause->
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

}
