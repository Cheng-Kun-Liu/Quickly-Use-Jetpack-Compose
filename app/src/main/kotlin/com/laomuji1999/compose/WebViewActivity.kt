package com.laomuji1999.compose

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.laomuji1999.compose.core.ui.screen.SlideActivity
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.feature.webview.WebViewScreenRoute
import com.laomuji1999.compose.feature.webview.WebViewScreenRoute.Companion.composeWebViewScreen
import com.laomuji1999.compose.feature.webview.WebViewScreenRoute.Companion.navigateToWebViewScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * 允许作为浏览器打开的Activity
 */
@AndroidEntryPoint
class WebViewActivity : SlideActivity() {
    companion object {
        fun openWebViewActivity(url: String, context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.data = url.toUri()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val startUrl = intent.data?.toString() ?: ""

        setContent {
            QuicklyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = WebViewScreenRoute(startUrl)
                ) {
                    composeWebViewScreen(
                        onBackClick = { finish() },
                        onOpenNewWindow = {
                            navController.navigateToWebViewScreen(it)
                        }
                    )
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        openWebViewActivity(intent.data.toString(), this)
    }

    override fun finish() {
        super.finish()
        val activityManager: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (appTask in activityManager.appTasks) {
            val isAllFinish = appTask.taskInfo.numActivities == 0
            val isSameName =
                componentName.shortClassName == appTask.taskInfo.baseIntent.component?.shortClassName
            if (isAllFinish && isSameName) {
                appTask.finishAndRemoveTask()
            }
        }
    }

}