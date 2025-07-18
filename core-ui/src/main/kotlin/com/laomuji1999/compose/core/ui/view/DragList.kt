package com.laomuji1999.compose.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.check.WeCheck

/**
 * 长按拖动排序
 * @author laomuji666
 * @since 2025/7/18
 */
@Composable
fun <T> DragList(
    modifier: Modifier = Modifier,
    list: List<T>,
    itemContent: @Composable BoxScope.(T, Boolean) -> Unit,
    onSwap: (a: Int, b: Int) -> Unit
) {
    var dragIndex: Int? by remember { mutableStateOf(null) }
    var dragItem: LazyListItemInfo? = remember { null }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val state = rememberLazyListState()
    LazyColumn(
        modifier = modifier.pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = { offset: Offset ->
                if (dragIndex == null) {
                    state.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                        offset.y.toInt() in item.offset..(item.offset + item.size)
                    }?.let {
                        (it.contentType as DragItemInfo?)?.let { dragItemInfo ->
                            dragIndex = dragItemInfo.index
                            dragItem = it
                        }
                    }
                    if (dragIndex == null) return@detectDragGesturesAfterLongPress
                }
            }, onDrag = { change, dragAmount: Offset ->
                change.consume()
                if (dragIndex == null || dragItem == null) return@detectDragGesturesAfterLongPress
                dragOffsetY += dragAmount.y
                val middleOffset =
                    ((dragItem!!.offset + dragOffsetY) + (dragItem!!.offset + dragItem!!.size + dragOffsetY)) / 2
                var targetIndex: Int? = null
                var targetItem: LazyListItemInfo? = null
                state.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                    var isFind = false
                    (item.contentType as DragItemInfo?)?.let { dragItemInfo ->
                        if (dragIndex != dragItemInfo.index && middleOffset.toInt() in item.offset..(item.offset + item.size)) {
                            isFind = true
                            targetIndex = dragItemInfo.index
                            targetItem = item
                        }
                    }
                    isFind
                }
                if (targetIndex == null) return@detectDragGesturesAfterLongPress
                dragOffsetY = -(targetItem!!.offset - dragItem!!.offset - dragOffsetY)
                onSwap(dragIndex!!, targetIndex!!)
                dragIndex = targetIndex
                dragItem = targetItem
            }, onDragCancel = {
                dragIndex = null
                dragItem = null
                dragOffsetY = 0f
            }, onDragEnd = {
                dragIndex = null
                dragItem = null
                dragOffsetY = 0f
            })
        }, state = state
    ) {
        itemsIndexed(
            list, contentType = { index, _ -> DragItemInfo(index = index) }) { index, item ->
            val itemModifier = if (index == dragIndex) {
                Modifier
                    .zIndex(1f)
                    .graphicsLayer {
                        translationY = dragOffsetY
                    }
            } else {
                Modifier
            }
            Box(
                modifier = itemModifier
            ) {
                itemContent(item, index == dragIndex)
            }
        }
    }
}

@Composable
fun DragListDemo(
    dragList: List<String>, onSwap: (a: Int, b: Int) -> Unit
) {
    DragList(
        modifier = Modifier
            .background(WeTheme.colorScheme.background)
            .fillMaxSize(),
        list = dragList,
        itemContent = { item, isDrag ->
            WeCheck(
                title = item,
                checked = isDrag,
                onCheckedChange = {}
            )
        },
        onSwap = onSwap
    )
}


/**
 * 需要记录原始的下标,LazyColumn不会保留所有的信息,只会保留可展示的item信息.
 */
private data class DragItemInfo(val index: Int)