package com.laomuji1999.compose.feature.http

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.view.ErrorView
import com.laomuji1999.compose.core.ui.view.LoadingDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.res.R

@Composable
fun HttpScreen(
    viewModel: HttpScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isError) {
        ErrorView {
            HttpScreen(
                viewModel = hiltViewModel(key = uniqueKey()),
            )
        }
        return
    }

    LoadingDialog(loading = uiState.isLoading)

    HttpScreenUi(
        uiState = uiState, onAction = viewModel::onAction
    )
}

@Composable
private fun HttpScreenUi(
    uiState: HttpScreenUiState,
    onAction: (HttpScreenAction) -> Unit,
) {
    WeScaffold(
        topBar = {
            WeTopBar(
                title = stringResource(id = R.string.string_demo_screen_http_demo),
            )
        }) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.string_http_screen_is_online, uiState.isConnect),
            style = WeTheme.typography.titleEm,
            color = WeTheme.colorScheme.fontColorHeavy,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        HttpScreenSlot(
            text = stringResource(id = R.string.string_http_screen_get_demo),
            onClick = { onAction(HttpScreenAction.GetListUsers) })
        HttpScreenSlot(
            text = stringResource(id = R.string.string_http_screen_post_demo),
            onClick = { onAction(HttpScreenAction.CreateUser) })
        Text(text = uiState.responseText, color = WeTheme.colorScheme.fontColorHeavy)
    }
}

@Composable
private fun HttpScreenSlot(
    text: String, onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        WeButton(
            weButtonType = WeButtonType.Big, weButtonColor = WeButtonColor.Primary, text = text
        ) {
            onClick()
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Preview
@Composable
fun PreviewHttpScreenUi() {
    QuicklyTheme {
        HttpScreenUi(
            uiState = HttpScreenUiState(), onAction = {})
    }
}

