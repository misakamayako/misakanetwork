import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val ktorm_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	kotlin("jvm") version "2.0.0-Beta3"
	id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0-Beta3"
	id("io.ktor.plugin") version "3.0.0-beta-1"
}

group = "cn.com.misakanetwork"
version = "0.0.1"
application {
	mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
	maven {
		setUrl("https://maven.aliyun.com/repository/public/")
	}
	maven {
		setUrl("https://maven.aliyun.com/repository/central/")
	}
	mavenCentral()
}

dependencies {

// 依赖注入
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("script-runtime"))

// 数据库及连接池
	implementation("com.alibaba:druid:1.2.8")
	implementation("mysql:mysql-connector-java:8.0.33")
	implementation("org.bouncycastle:bcprov-jdk15on:1.70")
	implementation("org.ktorm:ktorm-core:$ktorm_version")
	implementation("org.ktorm:ktorm-support-mysql:$ktorm_version")
	implementation("redis.clients:jedis:5.1.0")

// 日志
	implementation("ch.qos.logback:logback-classic:$logback_version")

// 阿里云对象存储服务
	implementation("com.aliyun.oss:aliyun-sdk-oss:3.16.3")

// Web框架
	implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-locations-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-html-builder-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-auto-head-response:$ktor_version")
	implementation("io.ktor:ktor-server-status-pages:$ktor_version")
	implementation("io.ktor:ktor-server-default-headers:$ktor_version")
	implementation("io.ktor:ktor-server-conditional-headers:$ktor_version")
	implementation("io.ktor:ktor-server-partial-content:$ktor_version")
	implementation("io.ktor:ktor-server-call-logging:$ktor_version")
	implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
	implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
	implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")
	implementation("io.ktor:ktor-server-request-validation:$ktor_version")

// 测试
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
	testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
	testImplementation("io.ktor:ktor-server-test-host-jvm:$ktor_version")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

// XML处理
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("javax.activation:activation:1.1.1")
	implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "21"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "21"
}
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}
