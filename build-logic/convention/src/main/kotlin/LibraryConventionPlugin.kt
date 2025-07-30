import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.libraryDefaultConfig
import util.libs

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
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

                //构建功能
                buildFeatures {
                    //开启compose支持
                    compose = true
                    //开启buildConfig支持
                    buildConfig = true
                }
            }

            dependencies {
                //compose依赖
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                //序列化
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}