plugins {
    alias(libs.plugins.laomuji1999.compose.library)
    alias(libs.plugins.laomuji1999.compose.hilt)
}

android {
    namespace = "com.laomuji1999.compose.core.logic.repository"
}

dependencies {

    //project
    implementation(project(":core-logic:common"))
    api(project(":core-logic:model"))
    implementation(project(":core-logic:network:http"))
    api(project(":core-logic:database"))
    implementation(project(":core-logic:notification"))

    //firebase
    implementation(platform(libs.firebase.bom))

    //google ai
    implementation(libs.generativeai)
}
