package com.laomuji1999.compose.core.ui.we.widget.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeTopBarAction(
    onActionClick: () -> Unit = {}, imageVector: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickableDebounce(
                indication = null, onClick = onActionClick
            )
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(WeTheme.colorScheme.fontColorHeavy),
            modifier = Modifier
                .align(Alignment.Center)
                .size(WeTheme.dimens.actionIconSize),
        )
    }
}