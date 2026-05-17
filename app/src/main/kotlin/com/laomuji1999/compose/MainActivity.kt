package com.laomuji1999.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.laomuji1999.compose.core.ui.screen.SlideActivity
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.feature.chat.ChatDeepLink
import com.laomuji1999.compose.feature.main.MainScreenRoute
import com.laomuji1999.compose.navigation.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
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
     * 监听 Intent 变化, 用于处理 DeepLink
     */
    private val intentFlow = MutableStateFlow<Intent?>(null)

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

        // 初始化时保存 Intent
        intentFlow.value = intent

        setContent {
            val backStack = rememberNavBackStack(MainScreenRoute)

            // 监听并处理 DeepLink
            val latestIntent by intentFlow.collectAsStateWithLifecycle()
            LaunchedEffect(latestIntent) {
                latestIntent?.let {
                    handleIntent(it, backStack)
                }
            }

            QuicklyTheme {
                NavigationHost(
                    backStack = backStack
                )
            }
        }

        viewModel.initModule(this)
    }

    /**
     * 当 Activity 处于后台被唤起时调用
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // 更新 Intent 以触发 LaunchedEffect
        intentFlow.value = intent
    }

    /**
     * 处理 DeepLink 导航逻辑
     */
    private fun handleIntent(intent: Intent, backStack: MutableList<NavKey>) {
        if (intent.action == Intent.ACTION_VIEW) {
            ChatDeepLink.parse(intent.data)?.forEach { route ->
                // 如果当前栈中已存在该路由, 则不再重复添加
                if (!backStack.contains(route)) {
                    backStack.add(route)
                }
            }
        }
        // 处理完后清空, 避免重复触发
        intentFlow.value = null
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
