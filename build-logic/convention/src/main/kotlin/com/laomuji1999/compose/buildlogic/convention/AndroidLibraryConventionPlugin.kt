package com.laomuji1999.compose.buildlogic.convention

import com.android.build.api.dsl.LibraryExtension
import com.laomuji1999.compose.buildlogic.config.libraryDefaultConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                libraryDefaultConfig()
            }
        }
    }
}
