plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.compose)
    alias(libs.plugins.laomuji1999.compose.serialization)
    alias(libs.plugins.laomuji1999.compose.hilt)
}

android {
    namespace = "com.laomuji1999.compose.feature.biometric"
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-logic:authenticate"))
}
