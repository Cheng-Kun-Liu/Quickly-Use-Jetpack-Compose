package com.laomuji1999.compose.feature.chat.me

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.switc.WeSwitch
import com.laomuji1999.compose.launcher.PermissionUtil
import com.laomuji1999.compose.res.R

@Composable
fun MeScreen(
    viewModel: MeScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionLauncher = PermissionUtil.getPostNotificationLauncher{
        viewModel.onAction(MeScreenAction.SwitchEnableNotification)
    }
    val hasPermission by rememberUpdatedState(PermissionUtil.hasPostNotificationPermission(context))
    MeScreenUi(
        enableNotification = uiState.enableNotification && hasPermission,
        onEnableNotificationClick = {
            permissionLauncher()
        }
    )
}

@Composable
fun MeScreenUi(
    enableNotification:Boolean,
    onEnableNotificationClick:()->Unit
){
    Column(
        modifier = Modifier
            .background(WeTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .background(WeTheme.colorScheme.rowBackground)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Row(modifier = Modifier
                .height(95.dp)
                .padding(WeTheme.dimens.rowPaddingHorizontal)) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(WeTheme.dimens.rowPaddingHorizontal))
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = WeTheme.dimens.rowPaddingHorizontal / 4)) {
                    Text(
                        text = stringResource(id = R.string.string_ai_chat_me_screen_we_chat_name),
                        style = WeTheme.typography.titleEm,
                        color = WeTheme.colorScheme.fontColorHeavy
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(id = R.string.string_ai_chat_me_screen_we_chat_number_format, stringResource(id = R.string.string_ai_chat_me_screen_we_chat_number)),
                        style = WeTheme.typography.body,
                        color = WeTheme.colorScheme.fontColorLight
                    )
                }
            }
            WeOutline(
                weOutlineType = WeOutlineType.Split
            )
        }
        WeOutline(
            weOutlineType = WeOutlineType.Full
        )
        WeSwitch(
            title = stringResource(id = R.string.string_ai_chat_me_screen_enable_notification),
            checked = enableNotification,
            onCheckedChange = {
                onEnableNotificationClick()
            },
        )
        WeOutline(
            weOutlineType = WeOutlineType.Full
        )
    }
}


@Preview
@Composable
fun PreviewMeScreenUi(){
    QuicklyTheme {
        MeScreenUi(
            enableNotification = true,
            onEnableNotificationClick = {}
        )
    }
}