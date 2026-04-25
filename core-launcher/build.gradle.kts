plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.compose)
}

android {
    namespace = "com.laomuji1999.compose.core.launcher"
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-logic:common"))

    //谷歌认证
    implementation(libs.play.services.auth)
}
