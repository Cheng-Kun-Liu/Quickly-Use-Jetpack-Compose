package com.laomuji1999.compose.feature.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.logic.authenticate.GoogleAuthenticate
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.core.ui.navigation.AppNavigationAction
import com.laomuji1999.compose.core.ui.view.LoadingDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetDialog
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.input.WeInput
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.title.WeTitle
import com.laomuji1999.compose.res.R

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onAction: (AppNavigationAction) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LoadingDialog(loading = uiState.isLoading)

    var showVideoDialog by remember { mutableStateOf(false) }
    if (showVideoDialog) {
        VideoPlayDialog(
            onDismissRequest = { showVideoDialog = false },
            onConfirm = { url ->
                onAction(AppNavigationAction.OnVideoPlayClick(url))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeOutline(weOutlineType = WeOutlineType.Full)
        WeTitle(title = stringResource(id = R.string.string_demo_group_sdk_samples))
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_ai_chat),
            onClick = { onAction(AppNavigationAction.OnAiChatClick) },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_video_play_demo),
            onClick = { showVideoDialog = true },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_web_view_demo),
            onClick = { onAction(AppNavigationAction.OnWebViewClick) },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_google_login_demo),
            onClick = {
                GoogleAuthenticate().requestLogin(
                    activityContext = context,
                    onSuccess = { email, idToken -> Toast.showText("$email $idToken") },
                    onFail = { Toast.showText("...") })
            }
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_http_demo),
            onClick = { onAction(AppNavigationAction.OnHttpClick) },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_firebase_demo),
            onClick = { onAction(AppNavigationAction.OnFirebaseClick) },
        )
        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}

@Composable
private fun VideoPlayDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var videoUrl by remember { mutableStateOf("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4") }
    var animateHide = { onDismissRequest() }
    WeActionSheetDialog(onDismissRequest = {
        animateHide()
    }) { state ->
        animateHide = {
            state.hide { onDismissRequest() }
        }
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.string_demo_screen_video_play_demo),
                style = WeTheme.typography.titleEm
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeInput(
                title = "URL",
                value = videoUrl,
                onValueChange = { videoUrl = it },
                tip = stringResource(id = R.string.string_demo_screen_video_play_url_tip)
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(
                text = stringResource(id = R.string.string_demo_screen_video_play_confirm),
                onClick = {
                    onConfirm(videoUrl)
                    onDismissRequest()
                },
                weButtonType = WeButtonType.Big
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
