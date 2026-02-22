package com.laomuji1999.compose.core.ui.extension.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * 支持Modifier拖动
 */
object ModifierDraggable {
    /**
     * 吸附模式
     * @param NONE 不开启
     * @param LEFT 左边
     * @param RIGHT 右边
     * @param BOTH 两边
     */
    enum class SnapMode { NONE, LEFT, RIGHT, BOTH }

    /**
     * 拖动方向
     * @param HORIZONTAL 水平方向
     * @param VERTICAL 垂直方向
     * @param ALL 全方向
     */
    enum class DragDirection { HORIZONTAL, VERTICAL, ALL }

    /**
     * Modifier拖动
     * @param bounded 是否边界
     * @param initOffsetX 初始X偏移量
     * @param initOffsetY 初始Y偏移量
     * @param enableSnap 是否开启吸附
     * @param enableSnapAnim 是否开启吸附动画
     * @param snapMode 吸附模式
     * @param dragDirection 拖动方向
     * @param onDrag 拖动回调
     */
    fun Modifier.draggable(
        bounded: Boolean = true,
        initOffsetX: Float = 0f,
        initOffsetY: Float = 0f,
        enableSnap: Boolean = false,
        enableSnapAnim: Boolean = true,
        snapMode: SnapMode = SnapMode.BOTH,
        dragDirection: DragDirection = DragDirection.ALL,
        onDrag: (Float, Float) -> Unit = { _, _ -> },
    ): Modifier = composed {
        val scope = rememberCoroutineScope()

        var parentSize by remember { mutableStateOf(IntSize.Zero) }
        var childSize by remember { mutableStateOf(IntSize.Zero) }

        val offsetX = remember { Animatable(initOffsetX) }
        val offsetY = remember { Animatable(initOffsetY) }
        val snapAnim: AnimationSpec<Float> = if (enableSnapAnim) {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
            )
        } else {
            tween(100)
        }

        LaunchedEffect(enableSnap, parentSize, childSize) {
            if (!enableSnap || parentSize.width <= 0) return@LaunchedEffect
            val targetX = when (snapMode) {
                SnapMode.LEFT -> 0f
                SnapMode.RIGHT -> (parentSize.width - childSize.width).toFloat()
                SnapMode.BOTH -> {
                    val middle = (parentSize.width - childSize.width) * 0.5f
                    if (offsetX.value < middle) 0f else (parentSize.width - childSize.width).toFloat()
                }

                SnapMode.NONE -> offsetX.value
            }
            offsetX.animateTo(targetX, snapAnim)
            onDrag(offsetX.value, offsetY.value)
        }

        this
            .onGloballyPositioned { coordinates ->
                parentSize = coordinates.parentLayoutCoordinates?.size ?: IntSize.Zero
                childSize = coordinates.size
            }
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .pointerInput(bounded, parentSize, childSize, dragDirection, snapMode, enableSnap) {
                detectDragGestures(onDragEnd = {
                    if (enableSnap) {
                        scope.launch {
                            val targetX = when (snapMode) {
                                SnapMode.LEFT -> 0f
                                SnapMode.RIGHT -> (parentSize.width - childSize.width).toFloat()
                                SnapMode.BOTH -> {
                                    val middle = (parentSize.width - childSize.width) * 0.5f
                                    if (offsetX.value < middle) 0f else (parentSize.width - childSize.width).toFloat()
                                }

                                SnapMode.NONE -> offsetX.value
                            }
                            offsetX.animateTo(targetX, snapAnim)
                            onDrag(offsetX.value, offsetY.value)
                        }
                    }
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    val deltaX =
                        if (dragDirection != DragDirection.VERTICAL) dragAmount.x else 0f
                    val deltaY =
                        if (dragDirection != DragDirection.HORIZONTAL) dragAmount.y else 0f

                    scope.launch {
                        val newX = offsetX.value + deltaX
                        val newY = offsetY.value + deltaY

                        if (bounded && parentSize != IntSize.Zero) {
                            offsetX.snapTo(
                                newX.coerceIn(
                                    0f, (parentSize.width - childSize.width).toFloat()
                                )
                            )
                            offsetY.snapTo(
                                newY.coerceIn(
                                    0f, (parentSize.height - childSize.height).toFloat()
                                )
                            )
                        } else {
                            offsetX.snapTo(newX)
                            offsetY.snapTo(newY)
                        }
                        onDrag(offsetX.value, offsetY.value)
                    }
                })
            }
    }
}