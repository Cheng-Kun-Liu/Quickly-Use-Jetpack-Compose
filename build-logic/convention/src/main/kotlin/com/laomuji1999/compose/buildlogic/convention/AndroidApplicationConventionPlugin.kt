package com.laomuji1999.compose.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.laomuji1999.compose.buildlogic.config.applicationDefaultConfig
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applicationPlugins()

            extensions.configure<ApplicationExtension> {
                applicationDefaultConfig()
                buildConfig()
            }

            signConfig()
        }
    }

    private fun Project.applicationPlugins() {
        with(pluginManager) {
            apply("com.android.application")
            apply("laomuji1999.compose.compose")
        }
    }

    private fun Project.signConfig() {
        extensions.configure<ApplicationExtension> {
            signingConfigs {
                create("gpSign") {
                    storeFile = file("../keystore/key1.jks")
                    keyAlias = "laomuji1999"
                    storePassword = "laomuji1999123"
                    keyPassword = "laomuji1999123"
                }
                create("samSign") {
                    storeFile = file("../keystore/key2.jks")
                    keyAlias = "laomuji1999"
                    storePassword = "laomuji1999123"
                    keyPassword = "laomuji1999123"
                }
            }
        }

        extensions.configure<ApplicationAndroidComponentsExtension> {
            onVariants { variant ->
                val flavorName = variant.flavorName ?: return@onVariants
                val signName = "${flavorName}Sign"
                val androidExt = extensions.getByType(ApplicationExtension::class.java)
                val sign = androidExt.signingConfigs.findByName(signName)
                    ?: throw GradleException(
                        "Missing signing config '$signName' for variant '${variant.name}'. " +
                            "Create signingConfigs.$signName or update the flavor-to-signing mapping."
                    )
                @Suppress("UnstableApiUsage")
                variant.signingConfig.setConfig(sign)
            }
        }
    }

    private fun ApplicationExtension.buildConfig() {
        buildFeatures {
            buildConfig = true
        }

        packaging {
            resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

        flavorDimensions += "channel"
        productFlavors {
            create("gp") {
                dimension = "channel"
                applicationId = "com.laomuji1999.compose"
            }
            create("sam") {
                dimension = "channel"
                applicationId = "com.laomuji1999.compose"
            }
        }

        buildTypes {
            debug {
                isMinifyEnabled = false
            }
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}
