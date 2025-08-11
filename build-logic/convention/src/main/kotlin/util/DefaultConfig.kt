package util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion

fun ApplicationExtension.applicationDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = DefaultConfigConstant.COMPILE_SDK
    defaultConfig {
        minSdk = DefaultConfigConstant.MIN_SDK
        targetSdk = DefaultConfigConstant.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
    }
}

fun LibraryExtension.libraryDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = DefaultConfigConstant.COMPILE_SDK
    defaultConfig {
        minSdk = DefaultConfigConstant.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

fun TestExtension.baselineprofileDefaultConfig() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdk = DefaultConfigConstant.COMPILE_SDK
    defaultConfig {
        //BaselineProfile最低只能在 28+ 才能运行
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //允许在模拟器上进行性能测试,模拟器性能不稳定,建议在真机上运行.
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }
}