package com.laomuji1999.compose.core.ui.we.widget.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.laomuji1999.compose.core.ui.view.ColumnListPicker
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetDialog
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.res.R

/**
 * 选择器条目数据结构
 * @param label 显示文本
 * @param children 子级条目列表，用于级联选择
 */
data class WePickerItem(
    val label: String,
    val children: List<WePickerItem> = emptyList()
)

/**
 * 级联选择时，父级改变后子级的处理策略
 */
enum class WePickerChangeStrategy {
    /** 重置为第一项 */
    Reset,

    /** 尽量保持当前下标，如果越界则取最大值 */
    Keep
}

/**
 * 通用级联选择器组件
 *
 * @param title 标题
 * @param data 级联数据源
 * @param columns 显示的列数
 * @param initPositions 初始选中位置列表
 * @param changeStrategy 级联切换策略
 * @param visibleCount 可见项数量，建议为奇数
 * @param itemHeight 每个条目的高度
 * @param onConfirm 确认回调，返回各列选中的索引位置
 * @param onCancel 取消回调
 * @param onDismissRequest 弹窗关闭请求
 */
@Composable
fun WePicker(
    title: String,
    data: List<WePickerItem>,
    columns: Int = 1,
    initPositions: List<Int> = emptyList(),
    changeStrategy: WePickerChangeStrategy = WePickerChangeStrategy.Reset,
    visibleCount: Int = 5,
    itemHeight: Dp = WeTheme.dimens.pickerItemHeight,
    onConfirm: (positions: List<Int>) -> Unit = {},
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = onCancel,
) {
    // 管理当前各列选中的位置状态
    var currentPositions by remember(data, columns) {
        mutableStateOf(
            if (initPositions.size == columns) initPositions
            else List(columns) { 0 }
        )
    }

    // 根据当前选中的位置，实时计算各列应该显示的数据源
    val columnsData = remember(data, currentPositions, columns) {
        val result = mutableListOf<List<WePickerItem>>()
        var currentLevel = data
        for (i in 0 until columns) {
            result.add(currentLevel)
            val selectedIndex =
                currentPositions.getOrNull(i)?.coerceIn(0, currentLevel.lastIndex.coerceAtLeast(0))
                    ?: 0
            currentLevel = if (currentLevel.isNotEmpty()) {
                currentLevel[selectedIndex].children
            } else {
                emptyList()
            }
        }
        result
    }

    WePickerContainer(
        title = title,
        visibleCount = visibleCount,
        itemHeight = itemHeight,
        onConfirm = { onConfirm(currentPositions) },
        onCancel = onCancel,
        onDismissRequest = onDismissRequest
    ) { h, v ->
        Row(modifier = Modifier.fillMaxWidth()) {
            columnsData.forEachIndexed { columnIndex, columnItems ->
                Box(modifier = Modifier.weight(1f)) {
                    // key 确保数据源改变时，对应的 ColumnListPicker 能够正确重置状态
                    key(columnItems) {
                        ColumnListPicker(
                            items = columnItems,
                            initPosition = currentPositions.getOrNull(columnIndex) ?: 0,
                            onItemSelected = { _, position ->
                                if (currentPositions[columnIndex] != position) {
                                    val newPositions = currentPositions.toMutableList()
                                    newPositions[columnIndex] = position

                                    // 联动处理：当当前列改变时，更新所有后续列的索引
                                    var currentChildren =
                                        columnItems.getOrNull(position)?.children ?: emptyList()
                                    for (j in columnIndex + 1 until columns) {
                                        val oldPos = newPositions[j]
                                        val newMax = currentChildren.lastIndex.coerceAtLeast(0)

                                        val nextPos = when (changeStrategy) {
                                            WePickerChangeStrategy.Keep -> oldPos.coerceIn(
                                                0,
                                                newMax
                                            )

                                            WePickerChangeStrategy.Reset -> 0
                                        }
                                        newPositions[j] = nextPos
                                        currentChildren =
                                            currentChildren.getOrNull(nextPos)?.children
                                                ?: emptyList()
                                    }
                                    currentPositions = newPositions
                                }
                            },
                            itemHeight = h,
                            showSize = v,
                            modifier = Modifier.fillMaxWidth()
                        ) { item, visibleIndex, index ->
                            Text(
                                text = item.label,
                                style = WeTheme.typography.body,
                                color = if (visibleIndex == index) {
                                    WeTheme.colorScheme.fontColorHeavy
                                } else {
                                    WeTheme.colorScheme.fontColorLight
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 选择器外部容器，处理标题、确认/取消按钮和弹窗样式
 */
@Composable
private fun WePickerContainer(
    title: String,
    itemHeight: Dp,
    visibleCount: Int,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable (itemHeight: Dp, visibleCount: Int) -> Unit
) {
    var animateHide: () -> Unit = {
        onDismissRequest()
    }
    WeActionSheetDialog(onDismissRequest = { animateHide() }) { state ->
        animateHide = {
            state.hide {
                onDismissRequest()
            }
        }
        Column(
            modifier = Modifier
                .background(WeTheme.colorScheme.secondaryButton)
                .fillMaxWidth()
                .padding(bottom = WeTheme.dimens.rowPaddingHorizontal)
        ) {
            // 标题栏
            Text(
                text = title,
                style = WeTheme.typography.title,
                color = WeTheme.colorScheme.fontColorHeavy,
                modifier = Modifier.padding(WeTheme.dimens.rowPaddingHorizontal)
            )

            // 选择器核心内容区，包含中间的高亮选中背景
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight * visibleCount),
                contentAlignment = Alignment.Center
            ) {
                // 中间选中项的高亮背景盒
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = WeTheme.dimens.rowInnerPaddingHorizontal)
                        .height(itemHeight)
                        .clip(RoundedCornerShape(WeTheme.dimens.buttonRondCornerDp))
                        .background(WeTheme.colorScheme.pressed)
                )
                content(itemHeight, visibleCount)
            }

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowPaddingHorizontal))

            // 底部按钮栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WeTheme.dimens.rowPaddingHorizontal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeButton(
                    text = stringResource(id = R.string.string_we_picker_cancel),
                    weButtonColor = WeButtonColor.Secondary,
                    weButtonType = WeButtonType.Small,
                    onClick = {
                        state.hide {
                            onCancel()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.padding(horizontal = WeTheme.dimens.rowInnerPaddingHorizontal))
                WeButton(
                    text = stringResource(id = R.string.string_we_picker_confirm),
                    weButtonColor = WeButtonColor.Primary,
                    weButtonType = WeButtonType.Small,
                    onClick = {
                        state.hide{
                            onConfirm()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
