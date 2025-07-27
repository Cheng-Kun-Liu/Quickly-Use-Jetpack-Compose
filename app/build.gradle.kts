plugins {
    alias(libs.plugins.laomuji1999.compose.application)
    alias(libs.plugins.laomuji1999.compose.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.baselineprofile)
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
    namespace = "com.laomuji1999.compose"

    //默认配置
    defaultConfig {
        applicationId = "com.laomuji1999.compose"
        versionCode = 12
        versionName = "1.2"
    }

    //使用不同的 build variant
    flavorDimensions += "channel"
    productFlavors {
        create("gp") {
            dimension = "channel"
            //这里也可以对包名后缀进行进一步区分
            // applicationIdSuffix = ".gp"
        }
        create("sam") {
            dimension = "channel"
        }
    }
}

dependencies {
    //compose 必须的依赖
    implementation(libs.androidx.lifecycle.runtime.compose)
    //关联 view 和 compose
    implementation(libs.androidx.activity.compose)

    //其它 module
    implementation(project(":core-ui"))
    implementation(project(":core-logic:common"))
    implementation(project(":core-logic:notification"))
    implementation(project(":core-logic:language"))

    implementation(project(":feature:demo"))
    implementation(project(":feature:firebase"))
    implementation(project(":feature:http"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:date"))
    implementation(project(":feature:scroll"))
    implementation(project(":feature:biometric"))
    implementation(project(":feature:painter"))
    implementation(project(":feature:video"))
    implementation(project(":feature:webview"))
    implementation(project(":feature:setting:language"))
    implementation(project(":feature:setting:font"))

    //firebase 崩溃分析
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    //引入 profileinstaller 用来在app首次启动时加载并安装 baseline profile
    implementation(libs.androidx.profileinstaller)
    //为 build variant 注册 baseline profile module
    "baselineProfile"(project(":baseline-profile"))

    //根据不同的渠道引入不同的 module
    "gpImplementation"(project(":flavor:flavor-gp"))
    "samImplementation"(project(":flavor:flavor-sam"))
}