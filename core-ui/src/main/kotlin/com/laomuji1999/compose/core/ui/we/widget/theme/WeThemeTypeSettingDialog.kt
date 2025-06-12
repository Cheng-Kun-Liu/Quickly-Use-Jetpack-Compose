package com.laomuji1999.compose.core.ui.we.widget.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.we.colorscheme.WeThemeColorType
import com.laomuji1999.compose.core.ui.we.colorscheme.WeThemeColorType.Companion.setWeThemeColorType
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheet
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetDialog
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetType
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType

@Composable
fun WeThemeSettingDialog(
    isShowDialog: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (!isShowDialog) {
        return
    }
    val selectedType by WeThemeColorType.currentWeThemeColorType.collectAsStateWithLifecycle()
    WeActionSheetDialog(
        onDismissRequest = onDismissRequest
    ) {
        val changeTheme: (WeThemeColorType) -> Unit = {
            setWeThemeColorType(it)
            hide { onDismissRequest() }
        }
        WeThemeTypeSettingDialogColorItem(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_system),
            selectedType = selectedType,
            targetType = WeThemeColorType.FlowSystem,
            onDismissRequest = {
                changeTheme(it)
            },
        )
        WeThemeTypeSettingDialogColorItem(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_light),
            selectedType = selectedType,
            targetType = WeThemeColorType.Light,
            onDismissRequest = {
                changeTheme(it)
            },
        )
        WeThemeTypeSettingDialogColorItem(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_dark),
            selectedType = selectedType,
            targetType = WeThemeColorType.Dark,
            onDismissRequest = {
                changeTheme(it)
            },
        )
        WeThemeTypeSettingDialogColorItem(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_blue),
            selectedType = selectedType,
            targetType = WeThemeColorType.Blue,
            onDismissRequest = {
                changeTheme(it)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.Split)
        WeActionSheet(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_cancel),
            onClick = {
                hide { onDismissRequest() }
            },
        )
    }
}

@Composable
private fun WeThemeTypeSettingDialogColorItem(
    text: String,
    selectedType: WeThemeColorType,
    targetType: WeThemeColorType,
    onDismissRequest: (WeThemeColorType) -> Unit,
) {
    WeActionSheet(
        text = text,
        weActionSheetType = if (selectedType == targetType) WeActionSheetType.Primary else WeActionSheetType.Normal,
        onClick = {
            onDismissRequest(targetType)
        },
    )
}