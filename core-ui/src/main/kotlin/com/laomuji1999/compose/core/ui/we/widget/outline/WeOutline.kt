package com.laomuji1999.compose.core.ui.we.widget.outline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeOutline(
    modifier: Modifier = Modifier,
    color: Color = WeTheme.colorScheme.outline,
    splitColor: Color = WeTheme.colorScheme.background,
    backgroundColor: Color = WeTheme.colorScheme.rowBackground,
    weOutlineType: WeOutlineType = WeOutlineType.Full,
) {
    when (weOutlineType) {
        WeOutlineType.None -> {}

        WeOutlineType.Full -> {
            Spacer(
                modifier = modifier
                    .height(WeTheme.dimens.outlineHeight)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .background(color)
            )
        }

        WeOutlineType.Split -> {
            Spacer(
                modifier = modifier
                    .height(WeTheme.dimens.outlineSplitHeight)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .background(splitColor)
            )
        }

        WeOutlineType.PaddingHorizontal -> {
            Spacer(
                modifier = modifier
                    .height(WeTheme.dimens.outlineHeight)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = WeTheme.dimens.rowPaddingHorizontal)
                    .background(color)
            )
        }

        WeOutlineType.PaddingStart -> {
            Spacer(
                modifier = modifier
                    .height(WeTheme.dimens.outlineHeight)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(start = WeTheme.dimens.rowPaddingHorizontal)
                    .background(color)
            )
        }

        is WeOutlineType.Custom -> {
            Spacer(
                modifier = modifier
                    .height(weOutlineType.height)
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(start = weOutlineType.start, end = weOutlineType.end)
                    .background(color)
            )
        }
    }
}