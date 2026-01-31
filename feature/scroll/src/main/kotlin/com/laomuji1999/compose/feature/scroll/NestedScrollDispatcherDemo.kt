package com.laomuji1999.compose.feature.scroll

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.res.R

/**
 * 子节点对父节点的滑动进行劫持
 */
@Composable
internal fun NestedScrollDispatcherScreen() {
    val density = LocalDensity.current

    val imageHeightMax = with(density) { 300.dp.toPx() }
    val imageHeightMin = with(density) { 100.dp.toPx() }
    var imageHeight by remember { mutableFloatStateOf(imageHeightMax) }

    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    val scrollState = rememberScrollableState { delta ->
        val newHeight = (imageHeight + delta).coerceIn(imageHeightMin, imageHeightMax)
        val consumed = newHeight - imageHeight
        imageHeight = newHeight
        val remaining = delta - consumed
        nestedScrollDispatcher.dispatchPostScroll(
            consumed = Offset(0f, consumed),
            available = Offset(0f, remaining),
            source = NestedScrollSource.UserInput
        )
        delta
    }

    WeScaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(30) {
                WeClick(title = "$it")
            }
            item {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(with(density) { imageHeight.toDp() })
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Vertical,
                            state = scrollState,
                            flingBehavior = ScrollableDefaults.flingBehavior(),
                            reverseDirection = false
                        )
                        .nestedScroll(
                            connection = remember { object : NestedScrollConnection {} },
                            dispatcher = nestedScrollDispatcher
                        )
                        .clickableDebounce {
                            if(imageHeight == imageHeightMax){
                                imageHeight = imageHeightMin
                            }else{
                                imageHeight = imageHeightMax
                            }
                        }
                )
            }
            items(30) {
                WeClick(title = "$it")
            }
        }
    }
}


@Preview
@Composable
private fun PreviewNestedScrollDispatcherScreen() {
    QuicklyTheme {
        WeScaffold {
            NestedScrollDispatcherScreen()
        }
    }
}