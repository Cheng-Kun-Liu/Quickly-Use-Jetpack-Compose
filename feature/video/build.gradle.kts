plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.hilt)
}

android {
    namespace = "com.laomuji1999.compose.feature.video"
}

dependencies {
    implementation(project(":core-ui"))

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui.compose)
    implementation(libs.androidx.media3.ui.compose.material3)

    implementation(libs.androidx.documentfile)
}
