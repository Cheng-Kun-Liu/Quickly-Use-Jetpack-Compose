import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.applicationDefaultConfig

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            //Application需要的插件
            applicationPlugins()

            extensions.configure<ApplicationExtension> {
                //application默认配置
                applicationDefaultConfig()

                //打包配置
                buildConfig(target)
            }
        }
    }

    private fun Project.applicationPlugins(){
        with(pluginManager){
            //基础插件
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.plugin.compose")

            //application需要的插件
            apply("com.android.application")
        }
    }

    private fun ApplicationExtension.buildConfig(project: Project){
        //构建功能
        buildFeatures {
            //开启compose支持
            compose = true
            //开启buildConfig支持
            buildConfig = true
        }

        //打包配置
        packaging {
            //构建资源文件
            resources {
                //排除文件路径,该路径的文件不会被打包.
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        //打包签名文件
        signingConfigs {
            getByName("debug") {
                storeFile = project.file("../keystore/key.jks")
                keyAlias = "laomuji1999"
                storePassword = "laomuji1999123"
                keyPassword = "laomuji1999123"
            }
            create("release") {
                storeFile = project.file("../keystore/key.jks")
                keyAlias = "laomuji1999"
                storePassword = "laomuji1999123"
                keyPassword = "laomuji1999123"
            }
        }
        //打包配置
        buildTypes {
            //测试配置
            debug {
                signingConfig = signingConfigs.getByName("debug")
                isMinifyEnabled = false
            }

            //发布配置,混淆代码
            release {
                signingConfig = signingConfigs.getByName("release")
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}