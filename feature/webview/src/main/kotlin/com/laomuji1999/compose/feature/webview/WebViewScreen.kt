package com.laomuji1999.compose.feature.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.isPreview
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.More
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBarAction

@Composable
fun WebViewScreen(
    viewModel: WebViewScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onOpenNewWindow: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WebViewScreenUi(
        uiState = uiState,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        onOpenNewWindow = onOpenNewWindow
    )
}

@Composable
private fun WebViewScreenUi(
    uiState: WebViewScreenUiState,
    onAction: (WebViewScreenAction) -> Unit,
    onBackClick: () -> Unit,
    onOpenNewWindow: (String) -> Unit,
) {
    val context = LocalContext.current
    var webView: WebView? by remember { mutableStateOf(null) }

    val webviewBack: () -> Unit = {
        webView?.run {
            if (canGoBack()) {
                goBack()
            } else {
                onBackClick()
                return@run
            }
            val urlList = copyBackForwardList()
            val endPosition = urlList.currentIndex
            var targetPosition = 0
            for (position in endPosition downTo 0) {
                val urlInfo = urlList.getItemAtIndex(position)
                if (urlInfo.url != "about:blank") {
                    targetPosition = endPosition - position
                    break
                }
                if (position == 0) {
                    onBackClick()
                    return@run
                }
            }
            goBackOrForward(targetPosition)
        } ?: onBackClick()
    }

    BackHandler {
        webviewBack()
    }
    WeScaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            webView = composeWebView(
                url = uiState.url,
                topPadding = WeTheme.dimens.topBarHeight,
                onProgressChanged = {
                    onAction(WebViewScreenAction.OnProgressChanged(it))
                },
                onOpenNewWindow = onOpenNewWindow,
                onReceivedTitle = {
                    onAction(WebViewScreenAction.OnReceivedTitle(it))
                },
                onUrlChanged = {
                    onAction(WebViewScreenAction.OnUrlChanged(it))
                }
            )
            WeTopBar(
                title = uiState.title,
                onBackClick = {
                    webviewBack()
                },
                actions = {
                    WeTopBarAction(
                        imageVector = WeIcons.More,
                        onActionClick = {
                            WebViewScreenUtil.useOtherAppOpen(url = uiState.url, context = context)
                        }
                    )
                }
            )
            if (uiState.progress < 100) {
                Spacer(
                    modifier = Modifier
                        .padding(top = WeTheme.dimens.topBarHeight)
                        .height(4.dp)
                        .fillMaxWidth(uiState.progress / 100f)
                        .background(WeTheme.colorScheme.cursorColor)
                )
            }
        }
    }

}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun composeWebView(
    url: String,
    topPadding: Dp,
    onProgressChanged: (Int) -> Unit,
    onOpenNewWindow: (String) -> Unit,
    onReceivedTitle: (String?) -> Unit,
    onUrlChanged: (String) -> Unit
): WebView? {
    if (isPreview()) {
        return null
    }
    var webView: WebView? by remember { mutableStateOf(null) }
    val paddingTopPx = with(LocalDensity.current) { topPadding.roundToPx() }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            FrameLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setPadding(0, paddingTopPx, 0, 0)
                webView = WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    //开启JavaScript
                    settings.javaScriptEnabled = true
                    //开启dom
                    settings.domStorageEnabled = true
                    //允许缩放
                    settings.setSupportZoom(true)
                    //支持ViewPort元标记
                    settings.useWideViewPort = true
                    //首次加载自动缩放到完整显示内容的比例
                    settings.loadWithOverviewMode = true
                    //混合加载网络协议
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    //支持多窗口模式
                    settings.setSupportMultipleWindows(true)
                    //设置没有用户的手势触摸也可以播放视频
                    settings.mediaPlaybackRequiresUserGesture = false

                    //设置WebViewClient,正常链接直接在当前WebView中打开
                    webViewClient = CustomWebViewClient(
                        openBrowser = {
                            if (url.startsWith("http") || url.startsWith("https")) {
                                loadUrl(url)
                            } else {
                                WebViewScreenUtil.useOtherAppOpen(url = url, context = context)
                            }
                        },
                        onUrlChanged = onUrlChanged
                    )
                    //设置WebChromeClient
                    webChromeClient = CustomWebChromeClient(
                        onProgressChanged = onProgressChanged,
                        onOpenNewWindow = { url ->
                            if (url.startsWith("http") || url.startsWith("https")) {
                                onOpenNewWindow(url)
                            } else {
                                WebViewScreenUtil.useOtherAppOpen(url = url, context = context)
                            }
                        },
                        onReceivedTitle = onReceivedTitle,
                    )
                }
                addView(webView)
            }
        },
        update = {
            if(webView?.url != url){
                webView?.loadUrl(url)
            }
        }
    )
    return webView
}