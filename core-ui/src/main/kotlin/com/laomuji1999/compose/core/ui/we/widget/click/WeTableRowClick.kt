package com.laomuji1999.compose.core.ui.we.widget.click

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Arrow
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow
import com.laomuji1999.compose.core.ui.we.widget.row.WeRowType

@Composable
fun WeClick(
    title: String,
    summary: String? = null,
    summaryInBottom: Boolean = false,
    onClick: () -> Unit = {},
) {
    WeRow(
        start = {
            if (summaryInBottom) {
                Column {
                    Text(
                        text = title,
                        style = WeTheme.typography.title,
                        color = WeTheme.colorScheme.fontColorHeavy
                    )
                    summary?.let {
                        Text(
                            text = it,
                            style = WeTheme.typography.body,
                            color = WeTheme.colorScheme.fontColorLight
                        )
                    }
                }
            } else {
                Text(
                    text = title,
                    style = WeTheme.typography.title,
                    color = WeTheme.colorScheme.fontColorHeavy
                )
            }
        },
        end = {
            if (!summaryInBottom) {
                summary?.let {
                    Text(
                        text = it,
                        style = WeTheme.typography.title,
                        color = WeTheme.colorScheme.fontColorLight
                    )
                    Spacer(modifier = Modifier.width(WeTheme.dimens.rowInnerPaddingHorizontal))
                }
            }
            Image(
                imageVector = WeIcons.Arrow,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(WeTheme.colorScheme.fontColorLight),
                modifier = Modifier.height(WeTheme.dimens.actionIconSize)
            )
        },
        weRowType = if (summaryInBottom) WeRowType.Double else WeRowType.Single,
        onClick = onClick,
    )
}