package com.laomuji1999.compose.buildlogic.convention

import com.laomuji1999.compose.buildlogic.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureAndroidHilt()
        }
    }

    private fun Project.configureAndroidHilt() {
        var configured = false
        val androidPluginIds = listOf(
            "com.android.application",
            "com.android.library",
            "com.android.test",
        )

        androidPluginIds.forEach { pluginId ->
            pluginManager.withPlugin(pluginId) {
                if (!configured) {
                    configured = true
                    pluginManager.apply("com.google.devtools.ksp")
                    dependencies {
                        add("ksp", libs.findLibrary("hilt.compiler").get())
                        add("implementation", libs.findLibrary("hilt.core").get())
                        add("implementation", libs.findLibrary("hilt.android").get())
                    }
                }
                pluginManager.apply("dagger.hilt.android.plugin")
            }
        }
    }
}
