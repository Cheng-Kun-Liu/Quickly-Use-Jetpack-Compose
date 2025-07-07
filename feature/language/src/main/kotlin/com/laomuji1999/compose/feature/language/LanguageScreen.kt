package com.laomuji1999.compose.feature.language

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.logic.AppLanguages
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.radio.WeRadio
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.feature.language.LanguageScreenRoute.Graph
import com.laomuji1999.compose.res.R

@Composable
fun LanguageScreen(
    viewModel: LanguageScreenViewModel = hiltViewModel(),
    navigateToGraph: (Graph) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.graph.collect {
            navigateToGraph(it)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LanguageScreenUi(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
    LaunchedEffect(Unit) {
        viewModel.updateLanguageStatus()
    }
}

@Composable
private fun LanguageScreenUi(
    uiState: LanguageScreenUiState,
    onAction: (LanguageScreenAction) -> Unit,
) {
    val context = LocalContext.current
    WeScaffold(
        topBar = {
            WeTopBar(
                title = stringResource(id = R.string.string_language_screen_title),
                onBackClick = {
                    onAction(LanguageScreenAction.OnClickBack)
                }
            )
        }
    ) {
        LazyColumn {
            item {
                WeOutline(weOutlineType = WeOutlineType.Full)
            }
            itemsIndexed(items = uiState.appLanguageList, key = { _, item ->
                item.tag
            }) { index, item ->
                WeRadio(
                    title = item.getDisplayName(context),
                    checked = item == uiState.usingLanguage,
                    onClick = {
                        onAction(LanguageScreenAction.OnLanguageClick(item, context))
                    },
                )
                if (index < uiState.appLanguageList.lastIndex) {
                    WeOutline(weOutlineType = WeOutlineType.PaddingStart)
                }
            }
            item {
                WeOutline(weOutlineType = WeOutlineType.Full)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLanguageScreenUi() {
    QuicklyTheme {
        var usingLanguage: AppLanguages by remember { mutableStateOf(AppLanguages.FlowSystem) }
        LanguageScreenUi(
            uiState = LanguageScreenUiState(usingLanguage = usingLanguage),
            onAction = {
                when (it) {
                    is LanguageScreenAction.OnLanguageClick -> usingLanguage = it.appLanguage
                    LanguageScreenAction.OnClickBack -> Unit
                }
            },
        )
    }
}