package cn.com.misakanetwork

import cn.com.misakanetwork.plugins.*
import cn.com.misakanetwork.route.router
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.util.pipeline.*


fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    statusPages()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    router()
    install(Helmet) {
        useDefault()
    }
    install(XForwardedHeaders)
    val beforeFallback = PipelinePhase("")
    insertPhaseBefore(ApplicationCallPipeline.Fallback, beforeFallback)
    intercept(beforeFallback) {
        if (call.response.status() == null) {
            throw NotFoundException()
        }
    }
}
