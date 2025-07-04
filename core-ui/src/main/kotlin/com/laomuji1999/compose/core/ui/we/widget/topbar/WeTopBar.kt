package com.laomuji1999.compose.core.ui.we.widget.topbar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Back
import com.laomuji1999.compose.core.ui.we.icons.WeIcons

@Composable
fun WeTopBar(
    title: String = "",
    onBackClick: (() -> Unit)? = null,
    enableBackHandle: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(WeTheme.colorScheme.background)
            .statusBarsPadding()
            .fillMaxWidth()
            .height(WeTheme.dimens.topBarHeight)
            .padding(horizontal = WeTheme.dimens.topBarPaddingHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            onBackClick?.let {
                BackHandler(enabled = enableBackHandle) {
                    it()
                }
                WeTopBarAction(
                    imageVector = WeIcons.Back, onActionClick = it
                )
            }
        }
        Text(
            text = title,
            style = WeTheme.typography.emTitle,
            color = WeTheme.colorScheme.fontColorHeavy,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End
        ) {
            actions()
        }
    }
}