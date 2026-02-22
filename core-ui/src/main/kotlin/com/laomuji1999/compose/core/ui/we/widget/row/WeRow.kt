package com.laomuji1999.compose.core.ui.we.widget.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeRow(
    modifier: Modifier = Modifier,
    start: @Composable RowScope.() -> Unit = {},
    center: @Composable RowScope.() -> Unit = { Spacer(modifier = Modifier.weight(1f)) },
    end: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = WeTheme.colorScheme.rowBackground,
    onClick: () -> Unit = {},
    weRowType: WeRowType = WeRowType.Single,
    paddingHorizontal: Dp = WeTheme.dimens.rowPaddingHorizontal,
) {
    val rowHeight = when (weRowType) {
        WeRowType.Single -> WeTheme.dimens.rowSingleHeight
        WeRowType.Double -> WeTheme.dimens.rowDoubleHeight
    }
    Column(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .clickableDebounce(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .padding(horizontal = paddingHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            start()
            center()
            end()
        }
    }
}