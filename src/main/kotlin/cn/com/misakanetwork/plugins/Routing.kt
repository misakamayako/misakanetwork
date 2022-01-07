package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Locations)

}

//@Location("/location/{name}")
//class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")
//@Location("/type/{name}")
//data class Type(val name: String) {
//    @Location("/edit")
//    data class Edit(val type: Type)
//
//    @Location("/list/{page}")
//    data class List(val type: Type, val page: Int)
//}
