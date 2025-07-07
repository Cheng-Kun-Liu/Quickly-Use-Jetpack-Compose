package com.laomuji1999.compose.core.ui.we.widget.switc

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline

@PreviewLightDark
@Composable
fun PreviewWeSwitch() {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(true) }
    QuicklyTheme {
        Column {
            WeSwitch(
                title = "勿扰模式",
                checked = checked1,
                onCheckedChange = { checked1 = it },
            )
            WeOutline()
            WeSwitch(title = "消息推送", checked = checked2, onCheckedChange = { checked2 = it })
        }
    }
}