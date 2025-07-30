plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.hilt)
}

android {
    namespace = "com.laomuji1999.compose.core.logic.network.http"

    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }
}

dependencies {
    //data model
    api(project(":core-logic:model"))
    //common
    api(project(":core-logic:common"))

    //ktor 跨平台 核心
    api(libs.ktor.client.core)
    //ktor 日志
    api(libs.ktor.client.logging)
    //ktor 序列化
    api(libs.ktor.client.content.negotiation)
    //ktor 序列化 json
    api(libs.ktor.serialization.kotlinx.json)
    //ktor okhttp 引擎
    api(libs.ktor.client.okhttp)
}
