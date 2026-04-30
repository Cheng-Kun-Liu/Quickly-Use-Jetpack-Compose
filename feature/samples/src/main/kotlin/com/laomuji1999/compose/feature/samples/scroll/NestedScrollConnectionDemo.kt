package com.laomuji1999.compose.feature.samples.scroll

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.res.R

/**
 * 同节点的滑动事件,必须由共同的父节点对滑动进行处理.
 */
@Composable
internal fun NestedScrollConnectionScreen() {
    val density = LocalDensity.current

    val imageHeightMax by remember {
        mutableFloatStateOf(with(density) {
            300.dp.toPx()
        })
    }
    val imageHeightMin by remember {
        mutableFloatStateOf(with(density) {
            100.dp.toPx()
        })
    }
    var imageHeight by remember {
        mutableFloatStateOf(imageHeightMax)
    }

    //添加高度变化动画, 避免太生�?
    val animHeight by animateFloatAsState(imageHeight, label = "")

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {
                if (source != NestedScrollSource.UserInput) {
                    return super.onPreScroll(available, source)
                }
                //上滑
                if (available.y < 0) {
                    val remainHeight = imageHeightMin - imageHeight
                    if (remainHeight < available.y) {
                        imageHeight += available.y
                        return Offset(0f, available.y)
                    } else {
                        imageHeight += remainHeight
                        return Offset(0f, remainHeight)
                    }
                }
                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                if (source != NestedScrollSource.UserInput) {
                    return super.onPreScroll(available, source)
                }
                //下滑
                if (available.y > 0) {
                    val remainHeight = imageHeightMax - imageHeight
                    if (remainHeight > available.y) {
                        imageHeight += available.y
                        return Offset(0f, available.y)
                    } else {
                        imageHeight += remainHeight
                        return Offset(0f, remainHeight)
                    }
                }
                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPreFling(
                available: Velocity
            ): Velocity {
                //重复上面的上�?
                if (available.y < 0) {
                    val remainHeight = imageHeightMin - imageHeight
                    if (remainHeight < available.y) {
                        imageHeight += available.y
                        return Velocity(0f, available.y)
                    } else {
                        imageHeight += remainHeight
                        return Velocity(0f, remainHeight)
                    }
                }
                return super.onPreFling(available)
            }

            override suspend fun onPostFling(
                consumed: Velocity, available: Velocity
            ): Velocity {
                //重复上面的下�?
                if (available.y > 0) {
                    val remainHeight = imageHeightMax - imageHeight
                    if (remainHeight > available.y) {
                        imageHeight += available.y
                        return Velocity(0f, available.y)
                    } else {
                        imageHeight += remainHeight
                        return Velocity(0f, remainHeight)
                    }
                }
                return super.onPostFling(consumed, available)
            }
        }
    }
    WeScaffold {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(
                    connection = nestedScrollConnection
                )
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .verticalScroll(rememberScrollState()) // 使图片本身也可以被滑�?
                    .height(with(density) {
                        animHeight.toDp()
                    })
                    .fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(300) {
                    WeClick(
                        title = "$it",
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewNestedScrollConnectionScreen() {
    QuicklyTheme {
        WeScaffold {
            NestedScrollConnectionScreen()
        }
    }
}
