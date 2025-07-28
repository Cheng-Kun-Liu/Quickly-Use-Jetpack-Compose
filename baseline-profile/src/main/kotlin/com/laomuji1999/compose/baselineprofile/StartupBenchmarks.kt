package com.laomuji1999.compose.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 性能测试,点击左边的运行,即可运行下方的所有性能测试.
 * 后缀名为 perfetto-trace 的性能追溯日志所在目录:
 * build\outputs\connected_android_test_additional_output\${BuildVariant}\connected\${DeviceName}
 * 上传一个日志到 https://ui.perfetto.dev/ 分析性能瓶颈
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    /**
     * 性能测试: 不使用编译优化.
     */
    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    /**
     * 性能测试: 启用部分编译优化, 使用 baseline profile.
     */
    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    /**
     * 基准测试公共函数,根据传入的编译模式进行启动性能测量.
     * @param compilationMode 指定编译模式，如无优化或基于 baseline profile 优化.
     */
    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = BaselineProfileMetrics.allMetrics,
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                //每次测试前回到主屏幕,保证冷启动.
                pressHome()
            },
            measureBlock = {
                //启动目标Activity并等待绘制首帧.
                startActivityAndWait()
            }
        )
    }
}