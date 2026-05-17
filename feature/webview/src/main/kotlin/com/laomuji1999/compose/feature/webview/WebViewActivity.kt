package com.laomuji1999.compose.feature.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.laomuji1999.compose.core.ui.extension.finishAndCleanupTask
import com.laomuji1999.compose.core.ui.extension.nav3PopBackStack
import com.laomuji1999.compose.core.ui.screen.SlideActivity
import com.laomuji1999.compose.core.ui.screen.SlideNavigation
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * WebViewActivity 是一个用于展示网页内容的 Activity。
 * 它继承自 [SlideActivity]，支持侧滑返回手势。
 * 该 Activity 被配置为可以处理 http/https 方案的 Intent，作为应用内的轻量级浏览器。
 */
@AndroidEntryPoint
class WebViewActivity : SlideActivity() {
    companion object {
        /**
         * 静态方法用于方便地从其他地方启动 WebViewActivity。
         *
         * @param url 要打开的网页地址。
         * @param context 启动 Activity 所需的上下文。
         */
        fun openWebViewActivity(url: String, context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.data = url.toUri()
            // 使用 NEW_TASK 标志确保 Activity 在其自己的任务栈中运行（配合 Manifest 中的 taskAffinity）
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启用全屏显示（边到边设计）
        enableEdgeToEdge()

        // 从 Intent 的 data 中获取初始加载的 URL
        val startUrl = intent.data?.toString() ?: ""

        setContent {
            QuicklyTheme {
                val backStack = rememberNavBackStack(WebViewScreenRoute(startUrl))
                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.nav3PopBackStack()
                        } else {
                            finish()
                        }
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                        rememberResultEventBusNavEntryDecorator()
                    ),
                    transitionSpec = SlideNavigation.nav3TransitionSpec(),
                    popTransitionSpec = SlideNavigation.nav3PopTransitionSpec(),
                    predictivePopTransitionSpec = SlideNavigation.nav3PredictivePopTransitionSpec()
                ) { key ->
                    when (key) {
                        is WebViewScreenRoute -> NavEntry(key) { route ->
                            WebViewScreen(
                                route = route as WebViewScreenRoute,
                                onBackClick = {
                                    if (backStack.size > 1) {
                                        backStack.nav3PopBackStack()
                                    } else {
                                        finish()
                                    }
                                },
                                onOpenNewWindow = {
                                    backStack.add(WebViewScreenRoute(it))
                                }
                            )
                        }
                        else -> NavEntry(key) {}
                    }
                }
            }
        }
    }

    /**
     * 当 Activity 已在运行且收到新的 Intent 时（例如从外部链接再次跳转回此 Activity）调用。
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 重新处理新的 URL 并刷新显示
        openWebViewActivity(intent.data.toString(), this)
    }

    /**
     * 重写 finish 方法以处理任务栈的清理。
     * 由于该 Activity 可能在独立的 Task 中运行，当 Activity 销毁后，
     * 检查并移除不再需要的任务项，避免在“最近任务”列表中留下空白页。
     */
    override fun finish() {
        super.finish()
        // 使用扩展函数清理任务栈卡片残留
        finishAndCleanupTask()
    }

}
