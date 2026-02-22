package com.laomuji1999.compose.core.ui.we.widget.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun RowScope.WeBottomBarItem(
    title: String,
    selected: Boolean,
    unSelectImageVector: ImageVector? = null,
    selectImageVector: ImageVector? = unSelectImageVector,
    onClick: () -> Unit
) {
    val color =
        if (selected) WeTheme.colorScheme.bottomBarSelect else WeTheme.colorScheme.bottomBarUnSelect
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickableDebounce(
                indication = null, onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selected) {
            selectImageVector?.let {
                Image(
                    imageVector = it,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color),
                    modifier = Modifier.size(WeTheme.dimens.bottomBarIconSize)
                )
            }
        } else {
            unSelectImageVector?.let {
                Image(
                    imageVector = it,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color),
                    modifier = Modifier.size(WeTheme.dimens.bottomBarIconSize)
                )
            }
        }
        Text(
            text = title, style = WeTheme.typography.caption, color = color
        )
    }
}