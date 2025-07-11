package com.laomuji1999.compose.core.ui.we.widget.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.laomuji1999.compose.core.ui.clickableDebounce
import com.laomuji1999.compose.core.ui.ifCondition
import com.laomuji1999.compose.core.ui.we.WeTheme

@Composable
fun WeButton(
    modifier: Modifier = Modifier,
    weButtonType: WeButtonType = WeButtonType.Small,
    weButtonColor: WeButtonColor = WeButtonColor.Primary,
    text: String,
    textStyle: TextStyle? = null,
    onClick: () -> Unit,
) {
    val buttonWidth = when (weButtonType) {
        WeButtonType.Big -> WeTheme.dimens.bigButtonWidth
        WeButtonType.Small -> WeTheme.dimens.smallButtonWidth
        WeButtonType.Warp -> 0.dp
    }
    val buttonHeight = when (weButtonType) {
        WeButtonType.Big -> WeTheme.dimens.bigButtonHeight
        WeButtonType.Small -> WeTheme.dimens.smallButtonHeight
        WeButtonType.Warp -> 0.dp
    }
    val buttonColor = when (weButtonColor) {
        WeButtonColor.Primary -> WeTheme.colorScheme.primaryButton
        WeButtonColor.Secondary -> WeTheme.colorScheme.secondaryButton
        WeButtonColor.Disable -> WeTheme.colorScheme.disableButton
        WeButtonColor.Wrong -> WeTheme.colorScheme.wrongButton
    }
    val textColor = when (weButtonColor) {
        WeButtonColor.Primary -> WeTheme.colorScheme.onPrimaryButton
        WeButtonColor.Secondary -> WeTheme.colorScheme.onSecondaryButton
        WeButtonColor.Disable -> WeTheme.colorScheme.onDisableButton
        WeButtonColor.Wrong -> WeTheme.colorScheme.onWrongButton
    }
    val style = textStyle ?: when (weButtonType) {
        WeButtonType.Big -> WeTheme.typography.titleEm
        WeButtonType.Small -> WeTheme.typography.title
        WeButtonType.Warp -> WeTheme.typography.bodyEm
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(WeTheme.dimens.buttonRondCornerDp))
            .defaultMinSize(minWidth = buttonWidth, minHeight = buttonHeight)
            .clickableDebounce(onClick = onClick)
            .background(buttonColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = style,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.ifCondition(
                condition = weButtonType == WeButtonType.Warp,
                onTrue = { padding(horizontal = WeTheme.dimens.warpButtonHorizontalPaddingDp, vertical = WeTheme.dimens.warpButtonVerticalPaddingDp) },
                onFalse = { padding(horizontal = 0.dp, vertical = 0.dp) })
        )
    }
}