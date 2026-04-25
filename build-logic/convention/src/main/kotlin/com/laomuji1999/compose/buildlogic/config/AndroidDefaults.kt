package com.laomuji1999.compose.buildlogic.config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion

fun ApplicationExtension.applicationDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = AndroidSdk.COMPILE
    defaultConfig {
        minSdk = AndroidSdk.MIN
        targetSdk = AndroidSdk.TARGET

        testInstrumentationRunner = AndroidDefaults.TEST_INSTRUMENTATION_RUNNER

        ndk {
            abiFilters += AndroidDefaults.ABI_FILTERS
        }
    }
}

fun LibraryExtension.libraryDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = AndroidSdk.COMPILE
    defaultConfig {
        minSdk = AndroidSdk.MIN
        testInstrumentationRunner = AndroidDefaults.TEST_INSTRUMENTATION_RUNNER
    }
}

fun TestExtension.baselineProfileDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = AndroidSdk.COMPILE
    defaultConfig {
        minSdk = AndroidSdk.BASELINE_PROFILE_MIN
        testInstrumentationRunner = AndroidDefaults.TEST_INSTRUMENTATION_RUNNER
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }
}

private object AndroidDefaults {
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val ABI_FILTERS = listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
}
