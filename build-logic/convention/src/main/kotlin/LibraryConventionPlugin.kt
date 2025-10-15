import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.libraryDefaultConfig
import util.libs

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                //基础插件
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")

                //library需要的插件
                apply("com.android.library")

                //序列化插件
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                libraryDefaultConfig()
                buildConfig()
            }

            dependencies {
                //compose依赖
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                //序列化
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }

    private fun LibraryExtension.buildConfig() {
        //构建功能
        buildFeatures {
            //开启compose支持
            compose = true
            //开启buildConfig支持
            buildConfig = true
        }
        //为所有的lib添加构建变量
        flavorDimensions += listOf("channel")
        productFlavors {
            create("gp") {
                dimension = "channel"
                buildConfigField("String","WEB_CLIENT_ID","\"954472126977-chps0hidiamvrln1ls96hqp4lgq14co6.apps.googleusercontent.com\"")
                buildConfigField("String","GEMINI_API_KEY","\"AIzaSyCuM1ecXRu37ZFy_DIQlIQWC9fkzkljKzg\"")
            }
            create("sam") {
                dimension = "channel"
                buildConfigField("String","WEB_CLIENT_ID","\"954472126977-chps0hidiamvrln1ls96hqp4lgq14co6.apps.googleusercontent.com\"")
                buildConfigField("String","GEMINI_API_KEY","\"AIzaSyCuM1ecXRu37ZFy_DIQlIQWC9fkzkljKzg\"")
            }
        }
    }
}