package com.laomuji1999.compose.buildlogic.convention

import com.laomuji1999.compose.buildlogic.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            pluginManager.withPlugin("com.android.application") {
                addSerializationDependencies()
            }

            pluginManager.withPlugin("com.android.library") {
                addSerializationDependencies()
            }
        }
    }

    private fun Project.addSerializationDependencies() {
        dependencies {
            add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
        }
    }
}
