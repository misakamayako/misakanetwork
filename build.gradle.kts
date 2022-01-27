val ktor_version: String by project
val ktorm_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
}

group = "cn.com.misakanetwork"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
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

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation(kotlin("script-runtime"))
}
