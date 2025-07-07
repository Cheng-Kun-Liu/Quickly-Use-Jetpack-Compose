package com.laomuji1999.compose.core.ui.we.widget.theme

import android.os.Build
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

    var animateHide: () -> Unit = {
        onDismissRequest()
    }
    WeActionSheetDialog(
        onDismissRequest = {
            animateHide()
        }
    ) { state ->
        animateHide = {
            state.hide {
                onDismissRequest()
            }
        }
        val changeTheme: (WeThemeColorType) -> Unit = {
            setWeThemeColorType(it)
            animateHide.invoke()
        }
        WeThemeTypeSettingDialogColorItem(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_system),
            selectedType = selectedType,
            targetType = WeThemeColorType.FlowSystem,
            onDismissRequest = {
                changeTheme(it)
            },
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //动态取色仅在Api31以上才可用
            WeThemeTypeSettingDialogColorItem(
                text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_dynamic),
                selectedType = selectedType,
                targetType = WeThemeColorType.Dynamic,
                onDismissRequest = {
                    changeTheme(it)
                },
            )
        }
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
        WeOutline(weOutlineType = WeOutlineType.Full)
        WeActionSheet(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_cancel),
            onClick = {
                animateHide()
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