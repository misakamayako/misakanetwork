package cn.com.misakanetwork.plugins

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlin.reflect.full.memberFunctions

open class Helmet {
    private lateinit var headers: MutableMap<String, String>

    class Configuration {
        internal val headers = mutableMapOf<String, String>()

        //        internal fun contentSecurityPolicy(options)
        @JvmOverloads
        internal fun crossOriginEmbedderPolicy(enable: Boolean = true) {
            this.headers["Cross-Origin-Embedder-Policy"] = "require-corp".takeIf { enable } ?: "unsafe-none"
        }

        enum class COOP {
            Disabled,
            Enable,
            AllowPop
        }

        internal fun crossOriginOpenerPolicy(policy: COOP = COOP.Enable) {
            this.headers["crossOriginOpenerPolicy"] = when (policy) {
                COOP.AllowPop -> "same-origin-allow-popups"
                COOP.Enable -> "same-origin"
                COOP.Disabled -> "unsafe-none"
            }
        }

        enum class CORP {
            SameSite,
            SameOrigin,
            CrossOrigin
        }

        internal fun crossOriginResourcePolicy(policy: CORP = CORP.SameOrigin) {
            this.headers["Cross-Origin-Resource-Policy"] = when (policy) {
                CORP.SameSite -> "same-site"
                CORP.SameOrigin -> " same-origin"
                CORP.CrossOrigin -> "cross-origin"
            }
        }

        internal class CTOption {
            internal var maxAge = 0L
            internal var enforce = false
            internal var reportTo: String? = null
        }

        internal fun expectCt(configure: CTOption.() -> Unit) {
            val option = CTOption().apply(configure)
            val value = StringBuilder("Expect-CT: max-age=${option.maxAge}")
            if (option.enforce) {
                value.append(", enforce")
            }
            if (option.reportTo != null) {
                value.append(", report-uri=\"${option.reportTo}\"")
            }
            this.headers["Expect-CT"] = value.toString()
        }

        enum class ReferrerPolicyOption(val value: String) {
            noReferrer("no-referrer"),
            noReferrerWhenDowngrade("no-referrer-when-downgrade"),
            origin("origin"),
            originWhenCrossOrigin("origin-when-cross-origin"),
            sameOrigin("same-origin"),
            strictOrigin("strict-origin"),
            strictOriginWhenCrossOrigin("strict-origin-when-cross-origin"),
            unsafeUrl("unsafe-url"),
        }

        internal class ReferrerPolicy {
            private val policy = StringBuilder()
            internal fun add(value: ReferrerPolicyOption) {
                if (policy.isNotEmpty()) {
                    policy.append(",")
                }
                policy.append(value.value)
            }

            fun getPolicy(): String {
                if (policy.isEmpty()) {
                    return "no-referrer"
                }
                return policy.toString()
            }
        }

        internal fun referrerPolicy(configure: ReferrerPolicy.() -> Unit) {
            val option = ReferrerPolicy().apply(configure)
            this.headers["Referrer-Policy"] = option.getPolicy()
        }

        class HstsOption {
            var maxAge = 15552000L
            var includeSubDomains = true
            var preload = false
        }

        internal fun hsts(configure: HstsOption.() -> Unit) {
            val option = HstsOption().apply(configure)
            val policy = StringBuilder(" max-age=${option.maxAge}")
            if (option.includeSubDomains) {
                policy.append("; includeSubDomains")
            }
            if (option.preload) {
                policy.append("; preload")
            }
            this.headers["Strict-Transport-Security"] = policy.toString()
        }

        internal fun noSniff() {
            this.headers["X-Content-Type-Options"] = "noSniff"
        }

        internal fun originAgentCluster() {
            this.headers["Origin-Agent-Cluster"] = "?1"
        }

        internal fun dnsPrefetchControl(allow: Boolean = false) {
            this.headers["X-DNS-Prefetch-Control"] = "on".takeIf { allow } ?: "off"
        }

        internal fun ieNoOpen() {
            this.headers["X-Download-Options"] = "noopen"
        }

        enum class FrameOptions(val value: String) {
            DENY("deny"),
            SAMEORIGIN("same-origin")
        }

        internal fun frameguard(option: FrameOptions = FrameOptions.DENY) {
            this.headers["X-Frame-Options"] = option.value
        }

        enum class PCDP(val value: String) {
            NONE("none"),
            MASTERONLY("master-only"),
            BYCONTENTTYPE("by-content-type"),
            ALL("all")
        }

        internal fun permittedCrossDomainPolicies(option: PCDP = PCDP.NONE) {
            this.headers["X-Permitted-Cross-Domain-Policies"] = option.value
        }

        internal fun xssFilter() {
            this.headers["X-XSS-Protection"] = "0"
        }
    }

    public companion object Feature : ApplicationFeature<Application, Configuration, Helmet> {
        override val key: AttributeKey<Helmet> = AttributeKey("Helmet")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): Helmet {
            val pipelinePhase = PipelinePhase("wear helmet")
            pipeline.insertPhaseBefore(ApplicationCallPipeline.Features, pipelinePhase)
            pipeline.intercept(pipelinePhase) {
                TODO("set headers")
//                this.call.response.headers.append("test", "dfsdf")
            }
            val configuration = Configuration().apply(configure)
            return Helmet().also { it.configure(configuration) }
        }
    }

    fun configure(configure: Configuration) {
//        Intrinsics.checkParameterIsNotNull
        if (configure.headers.isEmpty()) {
            TODO("invoke all methods to add default header")
            println(configure.headers.size)
        }
        this.headers = configure.headers
    }
}