package com.laomuji1999.compose.core.ui.we.widget.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeTitle(
    modifier: Modifier = Modifier,
    title:String
){
    Row(
        modifier = modifier.background(Color.Transparent).fillMaxWidth().padding(horizontal = WeTheme.dimens.rowPaddingHorizontal)
    ) {
        Text(
            text = title,
            style = WeTheme.typography.body,
            color = WeTheme.colorScheme.fontColorLight,
            modifier = Modifier.padding(vertical = WeTheme.dimens.rowInnerPaddingHorizontal)
        )
    }
}