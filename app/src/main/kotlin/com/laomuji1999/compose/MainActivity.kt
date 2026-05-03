package com.laomuji1999.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.laomuji1999.compose.core.ui.screen.SlideActivity
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.navigation.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * [AndroidEntryPoint] hilt依赖注入入口
 */
@AndroidEntryPoint
class MainActivity : SlideActivity() {

    /**
     * 注入ViewModel
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * 设置Activity的一些参数
     * 启动屏幕 保持到初始化完毕
     * Compose边缘到边缘 沉浸式状态栏导航栏
     * Compose的入口
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        checkKeepOnScreenCondition(splashScreen = splashScreen)

        enableEdgeToEdge()

        setContent {
            val navHostController = rememberNavController()
            QuicklyTheme {
                NavigationHost(
                    navHostController = navHostController
                )
            }
        }

        viewModel.initModule(this)
    }

    /**
     * 检查是否需要保留启动屏幕
     * 默认在setContent后就不再显示启动屏幕
     * 如果需要在启动时初始化一些三方SDK,可以保留启动屏幕到初始化完成后再显示
     */
    private fun checkKeepOnScreenCondition(splashScreen: SplashScreen) {
        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect {
                        uiState = it
                    }
            }
        }
        splashScreen.setKeepOnScreenCondition {
            uiState == MainUiState.Loading
        }
    }
}
