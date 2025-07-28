package com.laomuji1999.compose.baselineprofile

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric

object BaselineProfileMetrics {
    /**
     * Jit即时编译耗时占比
     */
    @OptIn(ExperimentalMetricApi::class)
    private val jitCompilationMetric = TraceSectionMetric("JIT Compiling %", label = "Jit即时编译")

    /**
     * Class加载初始化耗时占比
     */
    @OptIn(ExperimentalMetricApi::class)
    private val classInitMetric = TraceSectionMetric("L%/%;", label = "Class加载初始化")

    /**
     * 所有性能指标
     */
    @OptIn(ExperimentalMetricApi::class)
    val allMetrics = listOf(StartupTimingMetric(), jitCompilationMetric, classInitMetric)
}