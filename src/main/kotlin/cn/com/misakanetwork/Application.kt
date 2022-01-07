package cn.com.misakanetwork

import cn.com.misakanetwork.plugins.*
import cn.com.misakanetwork.route.router
import io.ktor.application.*


fun main(args: Array<String>) {
    println(args.joinToString(","))
    io.ktor.server.netty.EngineMain.main(args)
}


@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    statusPages()
    authorization()
    configureRouting()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    router()
}
