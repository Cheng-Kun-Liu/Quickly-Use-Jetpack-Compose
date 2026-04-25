package com.laomuji1999.compose.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.laomuji1999.compose.buildlogic.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    buildFeatures.compose = true
                }
                addComposeDependencies()
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures.compose = true
                }
                addComposeDependencies()
            }
        }
    }

    private fun Project.addComposeDependencies() {
        dependencies {
            add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
        }
    }
}
