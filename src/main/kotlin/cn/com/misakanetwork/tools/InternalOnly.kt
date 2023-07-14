package cn.com.misakanetwork.tools

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import io.ktor.server.netty.NettyApplicationRequest
suspend fun <TSubject : Any> PipelineContext<TSubject, ApplicationCall>.InternalOnly(
	next: suspend () -> TSubject,
) {
	println(this.context.request.local.uri=="127.0.0.1")
	next()
}
