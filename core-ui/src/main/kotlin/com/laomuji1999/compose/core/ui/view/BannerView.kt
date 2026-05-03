package com.laomuji1999.compose.core.ui.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.laomuji1999.compose.res.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 支持任意数据类型的banner
 *
 * @param modifier Modifier
 * @param bannerList Banner的数据源
 * @param initialPage 初始页
 * @param animDurationMillis 动画时长
 * @param scrollTimeMillis 滑动时间
 * @param looperBanner 是否循环
 * @param bannerType 滑动方向
 * @param xScale 非当前项的x轴缩放
 * @param yScale 非当前项的y轴缩放
 * @param contentPaddingHorizontal item的横向padding.
 * @param looperBufferSize 循环时buffer的大小,越大越好.
 * @param content 内容
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun <T> BannerView(
    modifier: Modifier = Modifier,
    bannerList: List<T>,
    initialPage: Int = 0,
    animDurationMillis: Int = 500,
    scrollTimeMillis: Int = animDurationMillis * 2,
    looperBanner: Boolean = true,
    bannerType: BannerType = BannerType.SCROLL_NEXT,
    xScale: Float = 0.95f,
    yScale: Float = 0.8f,
    contentPaddingHorizontal: Dp = 20.dp,
    looperBufferSize: Int = 10,
    content: @Composable (T) -> Unit
) {
    val uiBannerList by remember(bannerList, looperBanner) {
        mutableStateOf(
            if (looperBanner) {
                bannerList.toMutableList().apply {
                    for (i in 1..looperBufferSize) {
                        add(0, this[this.size - i])
                    }

                    for (i in 0 until looperBufferSize) {
                        add(this[looperBufferSize + i])
                    }
                }
            } else {
                bannerList
            })
    }

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = if (looperBanner) initialPage + looperBufferSize else initialPage,
        pageCount = { uiBannerList.size },
    )

    LaunchedEffect(pagerState.currentPage) {
        if (looperBanner) {
            coroutineScope.launch {
                val currentPage = pagerState.currentPage
                when (currentPage) {
                    0 -> {
                        //兜底,用户不停的滑动,直到第一个
                        pagerState.scrollToPage(page = looperBufferSize + 1)
                    }

                    uiBannerList.size - 1 -> {
                        //兜底,用户不停的滑动,直到最后一个
                        pagerState.scrollToPage(page = looperBufferSize)
                    }

                    else -> {
                        //looperBufferSize <= 正确下标 <= uiBannerList.size - 1 - looperBufferSize
                        //若当前下标不是正确下标,在动画播放完毕后,需要跳转到正确下标
                        val calcCorrectPage = {
                            when {
                                pagerState.currentPage < looperBufferSize -> {
                                    val relativeIndex = pagerState.currentPage
                                    uiBannerList.size - 2 * looperBufferSize + relativeIndex
                                }

                                pagerState.currentPage >= uiBannerList.size - looperBufferSize -> {
                                    val relativeIndex =
                                        pagerState.currentPage - (uiBannerList.size - looperBufferSize)
                                    looperBufferSize + relativeIndex
                                }

                                else -> null
                            }
                        }

                        calcCorrectPage()?.let {
                            delay(animDurationMillis.toLong())
                            if (it == calcCorrectPage()) {
                                pagerState.scrollToPage(page = it)
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        while (isActive) {
            delay(scrollTimeMillis.toLong())
            coroutineScope.launch {
                when (bannerType) {
                    BannerType.NONE -> Unit
                    BannerType.SCROLL_NEXT -> {
                        val nextPage = pagerState.calcNextPage()
                        pagerState.animateScrollToPage(nextPage)
                    }

                    BannerType.SCROLL_LAST -> {
                        val lastPage = pagerState.calcLastPage()
                        pagerState.animateScrollToPage(lastPage)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = contentPaddingHorizontal),
        ) { page ->
            val isSelected = page == pagerState.currentPage
            val xScaleAnim by getScaleAnim(isSelected, xScale, animDurationMillis)
            val yScaleAnim by getScaleAnim(isSelected, yScale, animDurationMillis)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = xScaleAnim
                        scaleY = yScaleAnim
                    },
            ) {
                content(uiBannerList[page])
            }
        }
    }
}

enum class BannerType {
    NONE, SCROLL_NEXT, SCROLL_LAST
}

@Composable
private fun getScaleAnim(isSelected: Boolean, scale: Float, animDurationMillis: Int): State<Float> {
    return animateFloatAsState(
        targetValue = if (isSelected) 1f else scale,
        animationSpec = tween(animDurationMillis),
        label = "scale"
    )
}

private fun PagerState.calcNextPage(): Int {
    return if (currentPage + 1 < pageCount) currentPage + 1 else 0
}

private fun PagerState.calcLastPage(): Int {
    return if (currentPage > 0) currentPage - 1 else pageCount - 1
}

@Preview
@Composable
fun PreviewBannerView() {
    BannerDemoView()
}

@Composable
fun BannerDemoView() {
    BannerView(
        modifier = Modifier.height(90.dp), bannerList = listOf(
            Color.Red, Color.Yellow, Color.Blue
        )
    ) {
        Box(
            modifier = Modifier
                .background(it)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )
        }
    }
}
