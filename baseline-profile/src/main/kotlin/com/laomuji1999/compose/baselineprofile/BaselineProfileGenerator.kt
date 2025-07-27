package com.laomuji1999.compose.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 必须在 release 的 build variant 运行,比如: gpRelease
 * 点击左边的运行按钮,会自动生成 baseline-prof.txt 文件
 * 在AGP8.2或更高版本,不需要手动操作 baseline-prof.txt 文件
 * 在构建的release文件中,会将所有的 baseline-prof.txt 合并成 baseline-prof 和 baseline.profm
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    /**
     * 基于 rule 的 profileBlock 生成 baseline-prof.txt 文件
     */
    @Test
    fun generate() {
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()
        }
    }
}