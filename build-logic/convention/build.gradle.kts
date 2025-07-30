import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.laomuji1999.compose.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    //仅在编译时需要的依赖,不会打包到包中.
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

tasks {
    validatePlugins {
        //启用更严格的验证,确保插件符合规范.
        enableStricterValidation = true
        //将构建的警告视为错误,如果出现构建失败,可以去掉.
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("hiltConvention") {
            id = "laomuji1999.compose.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("applicationConvention"){
            id = "laomuji1999.compose.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("libraryConvention"){
            id = "laomuji1999.compose.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("baselineprofile"){
            id = "laomuji999.compose.baselineprofile"
            implementationClass = "BaselineProfilePlugin"
        }
    }
}
