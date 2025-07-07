package com.laomuji1999.compose.core.ui.we.widget.check

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Select
import com.laomuji1999.compose.core.ui.we.icons.Unselect
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow

@Composable
fun WeCheck(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    WeRow(
        start = {
            Image(
                imageVector = if (checked) WeIcons.Select else WeIcons.Unselect,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(WeTheme.dimens.actionIconSize)
            )
            Spacer(modifier = Modifier.width(WeTheme.dimens.rowPaddingHorizontal))
            Text(
                text = title,
                style = WeTheme.typography.title,
                color = WeTheme.colorScheme.fontColorHeavy
            )
        },
        onClick = {
            onCheckedChange(!checked)
        },
    )
}