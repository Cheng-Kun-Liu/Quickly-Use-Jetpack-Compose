import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.applicationDefaultConfig

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            //Application需要的插件
            applicationPlugins()

            extensions.configure<ApplicationExtension> {
                //application默认配置
                applicationDefaultConfig()

                //打包配置
                buildConfig()
            }

            signConfig()
        }
    }

    private fun Project.applicationPlugins() {
        with(pluginManager) {
            //基础插件
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.plugin.compose")

            //application需要的插件
            apply("com.android.application")
        }
    }

    private fun Project.signConfig(){
        //创建SignConfig文件
        extensions.configure<ApplicationExtension> {
            signingConfigs {
                create("gpSign") {
                    storeFile = file("../keystore/key1.jks")
                    keyAlias = "laomuji1999"
                    storePassword = "laomuji1999123"
                    keyPassword = "laomuji1999123"
                }
                create("samSign") {
                    storeFile = file("../keystore/key2.jks")
                    keyAlias = "laomuji1999"
                    storePassword = "laomuji1999123"
                    keyPassword = "laomuji1999123"
                }
            }
        }

        //Flavor匹配对应的Sign.
        extensions.configure<ApplicationAndroidComponentsExtension> {
            onVariants { variant ->
                val flavorName = variant.flavorName ?: return@onVariants
                val signName = "${flavorName}Sign"
                val androidExt = extensions.getByType(ApplicationExtension::class.java)
                val sign = androidExt.signingConfigs.findByName(signName)
                @Suppress("UnstableApiUsage")
                variant.signingConfig.setConfig(sign!!)
            }
        }
    }

    private fun ApplicationExtension.buildConfig() {
        buildFeatures {
            compose = true
            buildConfig = true
        }

        packaging {
            resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

        flavorDimensions += "channel"
        productFlavors {
            create("gp") {
                dimension = "channel"
                applicationId = "com.laomuji1999.compose"
            }
            create("sam") {
                dimension = "channel"
                applicationId = "com.laomuji1999.compose"
            }
        }

        buildTypes {
            debug {
                isMinifyEnabled = false
            }
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

}