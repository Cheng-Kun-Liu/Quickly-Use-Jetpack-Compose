plugins {
    alias(libs.plugins.laomuji666.compose.application)
    alias(libs.plugins.laomuji666.compose.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

//自定义 Gradle Plugin, 在运行app的 build.gradle.kts 文件时,会被调用
apply<AppHelloWorldPlugin>()
class AppHelloWorldPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("AppHelloWorldPlugin apply")
    }
}

//其它AndroidApplication相关的配置在[ApplicationConventionPlugin]
android {
    //命名空间,尽量和applicationId一致,涉及到一些文件的路径.
    namespace = "com.laomuji666.compose"

    //默认配置
    defaultConfig {
        applicationId = "com.laomuji666.compose"
        versionCode = 12
        versionName = "1.2"
    }

    //使用不同的 build variant
    flavorDimensions += "channel"
    productFlavors {
        create("gp"){
            dimension = "channel"
            //这里也可以对包名后缀进行进一步区分
            // applicationIdSuffix = ".gp"
        }
        create("sam"){
            dimension = "channel"
        }
    }

    androidComponents {
        //强制使用传统打包方式,兼容 youtubedl
        onVariants(selector().all()) { variant ->
            @Suppress("UnstableApiUsage")
            variant.packaging.jniLibs.useLegacyPackaging = true
        }
    }
}

dependencies {
    //compose必须的依赖
    implementation(libs.androidx.lifecycle.runtime.compose)
    //关联view和compose
    implementation(libs.androidx.activity.compose)

    //其它module
    implementation(project(":core-ui"))
    implementation(project(":core-logic:common"))
    implementation(project(":core-logic:notification"))
    implementation(project(":feature:demo"))
    implementation(project(":feature:firebase"))
    implementation(project(":feature:http"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:date"))
    implementation(project(":feature:scroll"))
    implementation(project(":feature:biometric"))
    implementation(project(":feature:painter"))
    implementation(project(":feature:youtubedl"))
    implementation(project(":feature:video"))
    implementation(project(":feature:webview"))
    implementation(project(":feature:template"))

    //firebase 崩溃分析
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    //根据不同的渠道引入不同的module
    val gpImplementation by configurations
    val samImplementation by configurations
    gpImplementation(project(":flavor:flavor-gp"))
    samImplementation(project(":flavor:flavor-sam"))
}