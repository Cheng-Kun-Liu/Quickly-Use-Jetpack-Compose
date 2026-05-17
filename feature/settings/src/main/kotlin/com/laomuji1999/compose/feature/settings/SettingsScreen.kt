package com.laomuji1999.compose.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.result.ResultEffect
import com.laomuji1999.compose.core.logic.AppLanguages
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
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    onNavigate: (SettingsScreenRouter) -> Unit,
) {
    var showThemeDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val usingLanguage by viewModel.usingLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ResultEffect<AppLanguages> {
        viewModel.updateLanguage(it)
    }

    WeThemeColorSchemeSettingDialog(
        isShowDialog = showThemeDialog,
        onDismissRequest = {
            showThemeDialog = false
        })
    SettingsScreenUi(
        languageDisplayName = usingLanguage.getDisplayName(context),
        onLanguageClick = {
            onNavigate(SettingsScreenRouter.OnLanguageClick)
        },
        onThemeClick = {
            showThemeDialog = true
        },
        onNavigate = onNavigate,
    )
}

@Composable
private fun SettingsScreenUi(
    languageDisplayName: String,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onNavigate: (SettingsScreenRouter) -> Unit,
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
            summary = languageDisplayName,
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
                onNavigate(SettingsScreenRouter.OnFontClick)
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
                languageDisplayName = "Default",
                onLanguageClick = {},
                onThemeClick = {},
                onNavigate = {},
            )
        }
    }
}
