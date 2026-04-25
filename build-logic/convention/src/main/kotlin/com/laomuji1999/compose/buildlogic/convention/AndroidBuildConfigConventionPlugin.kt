package com.laomuji1999.compose.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.laomuji1999.compose.buildlogic.config.addDemoBuildConfigFields
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidBuildConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    configureBuildConfigFields()
                }
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    configureBuildConfigFields()
                }
            }
        }
    }

    private fun ApplicationExtension.configureBuildConfigFields() {
        buildFeatures.buildConfig = true
        defaultConfig {
            addDemoBuildConfigFields()
        }
    }

    private fun LibraryExtension.configureBuildConfigFields() {
        buildFeatures.buildConfig = true
        defaultConfig {
            addDemoBuildConfigFields()
        }
    }
}
