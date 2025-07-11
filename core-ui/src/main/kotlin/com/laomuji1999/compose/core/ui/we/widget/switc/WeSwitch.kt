package com.laomuji1999.compose.core.ui.we.widget.switc

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow

@Composable
fun WeSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    WeRow(
        start = {
            Text(
                text = title,
                style = WeTheme.typography.title,
                color = WeTheme.colorScheme.fontColorHeavy
            )
        }, end = {
            WeSwitchIcon(
                checked = checked
            )
        }, onClick = {
            onCheckedChange(!checked)
        }
    )
}

@Composable
private fun WeSwitchIcon(
    checked: Boolean,
    width: Dp = WeTheme.dimens.switchIconWidth,
    height: Dp = WeTheme.dimens.switchIconHeight,
    thumbSize: Dp = height * 0.85f,
    gap: Dp = height / 9,
) {
    val offsetX by animateDpAsState(
        targetValue = if (checked) width - thumbSize - gap else gap,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(if (checked) WeTheme.colorScheme.switchSelectBackground else WeTheme.colorScheme.switchUnSelectBackground)
    ) {
        Spacer(
            modifier = Modifier
                .size(thumbSize)
                .offset {
                    IntOffset(offsetX.roundToPx(), 0)
                }
                .shadow(elevation = gap, shape = CircleShape)
                .background(WeTheme.colorScheme.switchThumbColor, shape = CircleShape))
    }
}

