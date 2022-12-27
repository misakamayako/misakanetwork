import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val ktorm_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "cn.com.misakanetwork"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "io.ktor.server.netty.EngineMain"))
        }
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.ktorm:ktorm-core:$ktorm_version")
    implementation("org.ktorm:ktorm-support-mysql:${ktorm_version}")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("com.alibaba:druid:1.2.8")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("com.vladsch.flexmark:flexmark-all:0.62.2")
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")

    implementation("com.aliyun.oss:aliyun-sdk-oss:3.13.2")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("javax.activation:activation:1.1.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
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

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation(kotlin("script-runtime"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
