package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Locations)
}
