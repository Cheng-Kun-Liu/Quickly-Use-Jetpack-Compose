import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.libs

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKsp()
            configureHiltDependencies()
            configureAndroidHiltPlugin()
        }
    }

    private fun Project.configureKsp() {
        pluginManager.apply("com.google.devtools.ksp")
    }

    private fun Project.configureHiltDependencies() {
        dependencies {
            add("ksp", libs.findLibrary("hilt.compiler").get())
            add("implementation", libs.findLibrary("hilt.core").get())
            add("implementation", libs.findLibrary("hilt.android").get())
        }
    }

    private fun Project.configureAndroidHiltPlugin() {
        val androidPluginIds = listOf(
            "com.android.application",
            "com.android.library",
            "com.android.test",
        )

        androidPluginIds.forEach { pluginId ->
            pluginManager.withPlugin(pluginId) {
                pluginManager.apply("dagger.hilt.android.plugin")
            }
        }
    }
}
