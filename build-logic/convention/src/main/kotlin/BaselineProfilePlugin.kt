import com.android.build.api.dsl.TestExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.baselineprofileDefaultConfig
import util.libs

class BaselineProfilePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                //BaselineProfile
                apply("com.android.test")
                apply("androidx.baselineprofile")

                //基础插件
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                baselineprofileDefaultConfig()
                buildConfig()
            }


            dependencies {
                add("implementation", libs.findLibrary("androidx.junit").get())
                add("implementation", libs.findLibrary("androidx.espresso.core").get())
                add("implementation", libs.findLibrary("androidx.uiautomator").get())
                add("implementation", libs.findLibrary("androidx.benchmark.macro.junit4").get())
                add("implementation", libs.findLibrary("androidx.profileinstaller").get())
            }
        }
    }

    private fun TestExtension.buildConfig() {
        //因为在 :app 里有 flavor 所以需要在这里创建对应的编译变量
        targetProjectPath = ":app"
        flavorDimensions += listOf("channel")
        productFlavors {
            create("gp") { dimension = "channel" }
            create("sam") { dimension = "channel" }
        }
    }
}