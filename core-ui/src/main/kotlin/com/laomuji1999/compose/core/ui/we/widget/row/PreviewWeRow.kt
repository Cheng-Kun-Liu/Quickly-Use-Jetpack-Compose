package com.laomuji1999.compose.core.ui.we.widget.row

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme

@PreviewLightDark
@Composable
fun PreviewWeRow() {
    QuicklyTheme {
        Column {
            WeRow(weRowType = WeRowType.Single)
            WeRow(weRowType = WeRowType.Double)
        }
    }
}
