package com.laomuji1999.compose.core.ui.we.widget.outline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme

@PreviewLightDark
@Composable
fun PreviewWeOutline() {
    QuicklyTheme {
        Column {
            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.None
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.Full
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.PaddingHorizontal
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.PaddingStart
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.Split,
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowSingleHeight))
            WeOutline(
                weOutlineType = WeOutlineType.Custom(
                    WeTheme.dimens.outlineHeight * 2,
                    WeTheme.dimens.rowPaddingHorizontal * 2,
                    WeTheme.dimens.rowPaddingHorizontal * 2,
                )
            )

            Spacer(modifier = Modifier.height(WeTheme.dimens.rowDoubleHeight))
        }
    }
}
