package com.laomuji1999.compose.core.ui.we.widget.radio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Checked
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow

@Composable
fun WeRadio(
    title: String,
    checked: Boolean,
    onClick: () -> Unit = {},
) {
    WeRow(
        start = {
            Text(
                text = title,
                style = WeTheme.typography.title,
                color = WeTheme.colorScheme.fontColorHeavy
            )
        },
        end = {
            Row(modifier = Modifier.size(WeTheme.dimens.actionIconSize)) {
                AnimatedVisibility(
                    visible = checked, enter = expandHorizontally(
                        animationSpec = tween(800), expandFrom = Alignment.Start
                    ), exit = fadeOut(animationSpec = tween(0))
                ) {
                    Image(
                        imageVector = WeIcons.Checked,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        onClick = onClick,
    )
}

