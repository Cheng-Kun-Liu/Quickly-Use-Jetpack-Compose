package com.laomuji1999.compose.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.laomuji1999.compose.core.ui.navigation.AppNavigationAction
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.theme.WeThemeColorSchemeSettingDialog
import com.laomuji1999.compose.core.ui.we.widget.title.WeTitle
import com.laomuji1999.compose.res.R

@Composable
fun SettingsScreen(
    onAction: (AppNavigationAction) -> Unit,
) {
    var showThemeDialog by rememberSaveable {
        mutableStateOf(false)
    }
    WeThemeColorSchemeSettingDialog(
        isShowDialog = showThemeDialog,
        onDismissRequest = {
            showThemeDialog = false
        })
    SettingsScreenUi(
        onLanguageClick = {
            onAction(AppNavigationAction.OnLanguageClick)
        },
        onThemeClick = {
            showThemeDialog = true
        },
        onAction = onAction,
    )
}

@Composable
private fun SettingsScreenUi(
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onAction: (AppNavigationAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeTitle(title = stringResource(id = R.string.string_demo_group_appearance_localization))
        WeOutline(weOutlineType = WeOutlineType.Full)
        WeClick(
            title = stringResource(id = R.string.string_language_screen_title),
            onClick = onLanguageClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_theme_dialog_title),
            onClick = onThemeClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_font_screen_title),
            onClick = {
                onAction(AppNavigationAction.OnFontClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    QuicklyTheme {
        WeScaffold {
            SettingsScreenUi(
                onLanguageClick = {},
                onThemeClick = {},
                onAction = {},
            )
        }
    }
}
