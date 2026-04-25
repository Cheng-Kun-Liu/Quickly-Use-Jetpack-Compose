package com.laomuji1999.compose.buildlogic.convention

import com.android.build.api.dsl.TestExtension
import com.laomuji1999.compose.buildlogic.config.baselineProfileDefaultConfig
import com.laomuji1999.compose.buildlogic.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidBaselineProfileConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("androidx.baselineprofile")
            }

            extensions.configure<TestExtension> {
                baselineProfileDefaultConfig()
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
        targetProjectPath = ":app"
        flavorDimensions += listOf("channel")
        productFlavors {
            create("gp") { dimension = "channel" }
            create("sam") { dimension = "channel" }
        }
    }
}
