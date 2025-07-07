package com.laomuji1999.compose.feature.main.settings

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
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.theme.WeThemeSettingDialog
import com.laomuji1999.compose.feature.main.MainScreenAction
import com.laomuji1999.compose.res.R

@Composable
fun SettingsScreen(
    onAction: (MainScreenAction) -> Unit
) {
    SettingsScreenUi(
        onAction = onAction
    )
}

@Composable
fun SettingsScreenUi(
    onAction: (MainScreenAction) -> Unit,
) {
    var showThemeSettingDialog by rememberSaveable { mutableStateOf(false) }
    WeThemeSettingDialog(showThemeSettingDialog) {
        showThemeSettingDialog = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeClick(
            title = stringResource(id = R.string.string_language_screen_title),
            onClick = {
                onAction(MainScreenAction.OnLanguageClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_theme_dialog_title),
            onClick = {
                showThemeSettingDialog = true
            },
        )


        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenUi() {
    QuicklyTheme {
        SettingsScreenUi(
            onAction = {})
    }
}