package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
//        json(contentType = ContentType.Application.FormUrlEncoded)
    }
}

