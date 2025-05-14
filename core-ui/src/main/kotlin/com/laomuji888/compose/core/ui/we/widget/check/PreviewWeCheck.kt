package com.laomuji888.compose.core.ui.we.widget.check

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.laomuji888.compose.core.ui.theme.QuicklyTheme
import com.laomuji888.compose.core.ui.we.widget.outline.WeOutlineType

@PreviewLightDark
@Composable
fun PreviewWeCheck() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }
    var checked3 by remember { mutableStateOf(false) }
    var checked4 by remember { mutableStateOf(true) }
    QuicklyTheme {
        Column {
            WeCheck(
                title = "Item1",
                checked = checked1,
                onCheckedChange = { checked1 = it },
                weOutlineType = WeOutlineType.PaddingHorizontal
            )
            WeCheck(
                title = "Item2",
                checked = checked2,
                onCheckedChange = { checked2 = it },
                weOutlineType = WeOutlineType.PaddingHorizontal,
            )
            WeCheck(
                title = "Item3",
                checked = checked3,
                onCheckedChange = { checked3 = it },
                weOutlineType = WeOutlineType.PaddingHorizontal,
            )
            WeCheck(
                title = "Item4",
                checked = checked4,
                onCheckedChange = { checked4 = it },
                weOutlineType = WeOutlineType.None
            )
        }
    }
}