package com.laomuji1999.compose.core.ui.we.widget.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeSlider(
    modifier: Modifier = Modifier,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    onValueChanged: (Int) -> Unit,
    backgroundHeight: Dp = WeTheme.dimens.outlineSplitHeight,
    thumbSize: Dp = WeTheme.dimens.actionIconSize,
    leftColor: Color = WeTheme.colorScheme.primaryButton,
    rightColor: Color = WeTheme.colorScheme.fontColorLight,
    thumbColor: Color = WeTheme.colorScheme.onPrimaryButton,
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var trackWidth by remember { mutableIntStateOf(1) }

    val density = LocalDensity.current

    LaunchedEffect(value, minValue, maxValue, step) {
        sliderPosition = calculateSliderPosition(value, minValue, maxValue)
    }

    val changeValue: (Float) -> Unit = { position ->
        val newAmount = mapPositionToValue(minValue, maxValue, position, step)
        sliderPosition = calculateSliderPosition(newAmount, minValue, maxValue)
        onValueChanged(newAmount)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
                .clip(CircleShape)
        ) {
            //未滑过区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(rightColor)
            )
            //已滑过区域
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(
                        with(density) {
                            (trackWidth * sliderPosition + thumbSize.toPx() / 2).coerceAtLeast(0f).toDp()
                        }
                    )
                    .background(leftColor)
            )

            //实际区域,监听滑动和点击
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, _ ->
                            val newPos = (change.position.x / trackWidth.toFloat()).coerceIn(0f, 1f)
                            changeValue(newPos)
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val newPos = (offset.x / trackWidth.toFloat()).coerceIn(0f, 1f)
                            changeValue(newPos)
                        }
                    }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { trackWidth = it.width },
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            with(density){
                                (trackWidth * sliderPosition).toInt() - (thumbSize / 2).roundToPx()
                            },
                            0
                        )
                    }
                    .clip(CircleShape)
                    .background(thumbColor)
                    .size(thumbSize)
            )
        }
    }
}

private fun mapPositionToValue(min: Int, max: Int, position: Float, step: Int): Int {
    val rawValue = min + (max - min) * position
    val steppedValue = ((rawValue - min + step / 2) / step).toInt() * step + min
    return steppedValue.coerceIn(min, max)
}

private fun calculateSliderPosition(value: Int, min: Int, max: Int): Float {
    return if (max == min) 0f else (value.coerceIn(min, max) - min).toFloat() / (max - min)
}
