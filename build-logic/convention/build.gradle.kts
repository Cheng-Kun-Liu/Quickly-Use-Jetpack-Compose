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
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidHiltConventionPlugin"
        }
        register("applicationConvention"){
            id = "laomuji1999.compose.application"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidApplicationConventionPlugin"
        }
        register("libraryConvention"){
            id = "laomuji1999.compose.library"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidLibraryConventionPlugin"
        }
        register("composeConvention"){
            id = "laomuji1999.compose.compose"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidComposeConventionPlugin"
        }
        register("serializationConvention"){
            id = "laomuji1999.compose.serialization"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.KotlinSerializationConventionPlugin"
        }
        register("buildConfigConvention"){
            id = "laomuji1999.compose.build.config"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidBuildConfigConventionPlugin"
        }
        register("baselineprofile"){
            id = "laomuji1999.compose.baselineprofile"
            implementationClass = "com.laomuji1999.compose.buildlogic.convention.AndroidBaselineProfileConventionPlugin"
        }
    }
}
