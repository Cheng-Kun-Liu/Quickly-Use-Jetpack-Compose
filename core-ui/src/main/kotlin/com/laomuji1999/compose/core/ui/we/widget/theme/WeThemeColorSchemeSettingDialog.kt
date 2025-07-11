package com.laomuji1999.compose.core.ui.we.widget.theme

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.we.cache.WeCacheColorScheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheColorScheme.Companion.setWeThemeColorType
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheet
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetDialog
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetType
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType

@Composable
fun WeThemeColorSchemeSettingDialog(
    isShowDialog: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (!isShowDialog) {
        return
    }
    val currentWeThemeColorType by WeCacheColorScheme.currentWeThemeColorType.collectAsStateWithLifecycle()

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
        val changedCallback: (WeCacheColorScheme) -> Unit = {
            setWeThemeColorType(it)
            animateHide.invoke()
        }
        Item(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_system),
            selectedType = currentWeThemeColorType,
            targetType = WeCacheColorScheme.FlowSystem,
            onDismissRequest = {
                changedCallback(it)
            },
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //动态取色仅在Api31以上才可用
            Item(
                text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_dynamic),
                selectedType = currentWeThemeColorType,
                targetType = WeCacheColorScheme.Dynamic,
                onDismissRequest = {
                    changedCallback(it)
                },
            )
        }
        Item(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_light),
            selectedType = currentWeThemeColorType,
            targetType = WeCacheColorScheme.Light,
            onDismissRequest = {
                changedCallback(it)
            },
        )
        Item(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_dark),
            selectedType = currentWeThemeColorType,
            targetType = WeCacheColorScheme.Dark,
            onDismissRequest = {
                changedCallback(it)
            },
        )
        Item(
            text = stringResource(com.laomuji1999.compose.res.R.string.string_theme_dialog_we_theme_blue),
            selectedType = currentWeThemeColorType,
            targetType = WeCacheColorScheme.Blue,
            onDismissRequest = {
                changedCallback(it)
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
private fun Item(
    text: String,
    selectedType: WeCacheColorScheme,
    targetType: WeCacheColorScheme,
    onDismissRequest: (WeCacheColorScheme) -> Unit,
) {
    WeActionSheet(
        text = text,
        weActionSheetType = if (selectedType == targetType) WeActionSheetType.Primary else WeActionSheetType.Normal,
        onClick = {
            onDismissRequest(targetType)
        },
    )
}