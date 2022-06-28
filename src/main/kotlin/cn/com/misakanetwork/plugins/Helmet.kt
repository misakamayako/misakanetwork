package cn.com.misakanetwork.plugins

import io.ktor.server.application.*

val Helmet = createApplicationPlugin(name = "Helmet", createConfiguration = ::PluginConfiguration) {
    pluginConfig.init()
    onCallRespond { call ->
        pluginConfig.headers.forEach { (t, u) ->
            call.response.headers.append(t, u)
        }
    }
}

enum class COOP {
    Disabled,
    Enable,
    AllowPop
}

enum class CORP {
    SameSite,
    SameOrigin,
    CrossOrigin
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

class PluginConfiguration {

    internal val headers = HashMap<String, String>()

    internal fun crossOriginEmbedderPolicy(enable: Boolean = true) {
        this.headers["Cross-Origin-Embedder-Policy"] = "require-corp".takeIf { enable } ?: "unsafe-none"
    }

    internal fun crossOriginOpenerPolicy(policy: COOP = COOP.Enable) {
        this.headers["crossOriginOpenerPolicy"] = when (policy) {
            COOP.AllowPop -> "same-origin-allow-popups"
            COOP.Enable -> "same-origin"
            COOP.Disabled -> "unsafe-none"
        }
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

    internal fun expectCt() {
        expectCt {}
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

    internal fun referrerPolicy() {
        referrerPolicy {}
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

    internal fun hsts() {
        hsts {}
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

    internal var applyDefault: Boolean = false
    internal fun useDefault() {
        applyDefault = true
    }

    fun init(): PluginConfiguration {
        if (this.applyDefault) {
//                contentSecurityPolicy()
            crossOriginEmbedderPolicy()
            crossOriginOpenerPolicy()
            crossOriginResourcePolicy()
            expectCt()
            referrerPolicy()
            hsts()
            noSniff()
            originAgentCluster()
            dnsPrefetchControl()
            ieNoOpen()
            frameguard()
            permittedCrossDomainPolicies()
            xssFilter()
        }
        return this
    }
}
