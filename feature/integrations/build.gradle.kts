plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.compose)
    alias(libs.plugins.laomuji1999.compose.serialization)
    alias(libs.plugins.laomuji1999.compose.hilt)
}

android {
    namespace = "com.laomuji1999.compose.feature.integrations"
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":core-logic:common"))
    implementation(project(":core-logic:repository"))
    implementation(project(":core-logic:analytics"))
    implementation(project(":core-logic:notification"))
    implementation(project(":core-logic:authenticate"))
    implementation(project(":core-launcher"))
}
