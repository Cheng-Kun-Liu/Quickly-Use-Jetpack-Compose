package com.laomuji1999.compose.core.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import kotlinx.coroutines.launch

/**
 * 纵向列表选择
 * @param modifier 修饰符
 * @param items 列表项
 * @param initPosition 初始位置
 * @param onItemSelected 选择项
 * @param itemHeight 每个item的高度
 * @param showSize 显示的item数量
 * @param item item内容
 */
@Composable
fun <T> ColumnListPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    initPosition: Int,
    onItemSelected: (item: T, position: Int) -> Unit,
    itemHeight: Dp,
    showSize: Int,
    item: @Composable (item: T, visibleItemIndex: Int, currentIndex: Int) -> Unit,
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initPosition)
    val coroutineScope = rememberCoroutineScope()
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    val visibleItemIndex by remember(items) {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            val centerIndexOffset = ((offset + itemHeightPx / 2) / itemHeightPx).toInt()
            (first + centerIndexOffset).coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            coroutineScope.launch {
                listState.animateScrollToItem(visibleItemIndex)
            }
        }
    }

    LaunchedEffect(visibleItemIndex) {
        onItemSelected(items[visibleItemIndex], visibleItemIndex)
    }

    Box(
        modifier = modifier.height(itemHeight * showSize),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = itemHeight * (showSize / 2)),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items, key = { _, item -> item.hashCode() }) { index, value ->
                Box(
                    modifier = Modifier
                        .clickableDebounce {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    item(value, visibleItemIndex, index)
                }
            }
        }
    }
}