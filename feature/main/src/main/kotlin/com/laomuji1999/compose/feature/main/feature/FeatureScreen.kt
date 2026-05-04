package com.laomuji1999.compose.feature.main.feature

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.logic.authenticate.GoogleAuthenticate
import com.laomuji1999.compose.core.logic.authenticate.biometric.BiometricAuthenticate
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.view.LoadingDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.actionsheet.WeActionSheetDialog
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.input.WeInput
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.switc.WeSwitch
import com.laomuji1999.compose.core.ui.we.widget.title.WeTitle
import com.laomuji1999.compose.feature.integrations.biometric.BiometricScreenAction
import com.laomuji1999.compose.feature.integrations.biometric.BiometricScreenViewModel
import com.laomuji1999.compose.feature.main.MainScreenAction
import com.laomuji1999.compose.launcher.OpenAlbum
import com.laomuji1999.compose.launcher.OpenCamera
import com.laomuji1999.compose.launcher.OpenContact
import com.laomuji1999.compose.launcher.PermissionUtil
import com.laomuji1999.compose.launcher.selectMobileLauncher
import com.laomuji1999.compose.res.R

@Composable
fun FeatureScreen(
    viewModel: FeatureScreenViewModel = hiltViewModel(),
    onAction: (MainScreenAction) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoadingDialog(loading = uiState.isLoading)

    val context = LocalContext.current

    val selectMobile = selectMobileLauncher(onSuccess = {
        Toast.showText(it)
    }, onFail = {
        Toast.showText("...")
    })

    val openAlbum = OpenAlbum.openAlbum()

    val openCamera = OpenCamera.openCamera()

    val openContact = OpenContact.openContact()

    val biometricLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    val launcherMultiplePermissions = PermissionUtil.getPermissionsLauncher(
        permissions = listOf(
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
        ), onCallback = { _, _, askDenied ->
            askDenied.apply {
                if (contains(ACCESS_FINE_LOCATION) && contains(ACCESS_COARSE_LOCATION)) {
                    Toast.showText(
                        context = context,
                        resId = R.string.string_permission_location_ask_denied,
                    )
                    PermissionUtil.Setting.openSetting(context)
                }
            }
        })

    var showVideoDialog by remember { mutableStateOf(false) }
    if (showVideoDialog) {
        VideoPlayDialog(
            onDismissRequest = { showVideoDialog = false },
            onConfirm = { url ->
                onAction(MainScreenAction.OnVideoPlayClick(url))
            }
        )
    }

    var showBiometricDialog by remember { mutableStateOf(false) }
    if (showBiometricDialog) {
        BiometricDialog(
            onDismissRequest = { showBiometricDialog = false },
            biometricLauncher = biometricLauncher
        )
    }

    FeatureScreenUi(
        uiState = uiState,
        onSwitchAppLogoClick = {
            viewModel.switchAppLogo(context)
        },
        onLocationClick = {
            viewModel.getLocation(context = context, onPermissionDenied = {
                launcherMultiplePermissions()
            })
        },
        onSelectMobileClick = {
            selectMobile()
        },
        onOpenAlbumClick = {
            openAlbum.launch {
                Toast.showText("$it")
            }
        },
        onOpenCameraClick = {
            openCamera.launch {
                Toast.showText("$it")
            }
        },
        onOpenContactClick = {
            openContact.launch {
                it?.let {
                    Toast.showText("${it.name} : ${it.mobile}")
                }
            }
        },
        onVideoPlayClick = {
            showVideoDialog = true
        },
        onBiometricClick = {
            showBiometricDialog = true
        },
        onAction = onAction,
    )
}

@Composable
private fun BiometricDialog(
    onDismissRequest: () -> Unit,
    biometricLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    viewModel: BiometricScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    WeActionSheetDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val titleText = when (val result = uiState.biometricResult) {
                BiometricAuthenticate.BiometricAuthenticateResult.HardwareNotFound -> stringResource(id = R.string.string_biometric_screen_status_hardware_not_found)
                BiometricAuthenticate.BiometricAuthenticateResult.HardwareUnavailable -> stringResource(id = R.string.string_biometric_screen_status_hardware_unavailable)
                BiometricAuthenticate.BiometricAuthenticateResult.NotSetBiometric -> stringResource(id = R.string.string_biometric_screen_status_hardware_not_set)
                is BiometricAuthenticate.BiometricAuthenticateResult.OtherError -> stringResource(
                    id = R.string.string_biometric_screen_status_hardware_other_error,
                    result.msg
                )

                BiometricAuthenticate.BiometricAuthenticateResult.Success -> stringResource(id = R.string.string_biometric_screen_status_hardware_success)
                BiometricAuthenticate.BiometricAuthenticateResult.Fail -> stringResource(id = R.string.string_biometric_screen_status_hardware_fail)
                null -> stringResource(id = R.string.string_demo_screen_biometric)
            }
            Text(
                text = titleText,
                style = WeTheme.typography.titleEm
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeInput(
                title = stringResource(id = R.string.string_biometric_screen_title_title),
                tip = stringResource(id = R.string.string_biometric_screen_title_tip),
                value = uiState.title,
                onValueChange = { viewModel.onAction(BiometricScreenAction.OnTitleChange(it)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            WeInput(
                title = stringResource(id = R.string.string_biometric_screen_description_title),
                tip = stringResource(id = R.string.string_biometric_screen_description_tip),
                value = uiState.description,
                onValueChange = { viewModel.onAction(BiometricScreenAction.OnDescriptionChange(it)) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(
                text = stringResource(id = R.string.string_biometric_screen_handle_title),
                onClick = { viewModel.onAction(BiometricScreenAction.HandleBiometric(context)) },
                weButtonType = WeButtonType.Big
            )
            if (uiState.biometricResult is BiometricAuthenticate.BiometricAuthenticateResult.NotSetBiometric) {
                Spacer(modifier = Modifier.height(20.dp))
                WeButton(
                    text = stringResource(id = R.string.string_biometric_screen_setting_title),
                    onClick = {
                        viewModel.onAction(BiometricScreenAction.OnSettingClick(biometricLauncher))
                    },
                    weButtonType = WeButtonType.Big,
                    weButtonColor = WeButtonColor.Secondary
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
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

@Composable
private fun FeatureScreenUi(
    uiState: FeatureScreenUiState,
    onSwitchAppLogoClick: () -> Unit,
    onLocationClick: () -> Unit,
    onSelectMobileClick: () -> Unit,
    onOpenAlbumClick: () -> Unit,
    onOpenCameraClick: () -> Unit,
    onOpenContactClick: () -> Unit,
    onVideoPlayClick: () -> Unit,
    onBiometricClick: () -> Unit,
    onAction: (MainScreenAction) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeTitle(title = stringResource(id = R.string.string_demo_group_core_features))
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_firebase_demo),
            onClick = {
                onAction(MainScreenAction.OnFirebaseClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_http_demo),
            onClick = {
                onAction(MainScreenAction.OnHttpClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_ai_chat),
            onClick = {
                onAction(MainScreenAction.OnAiChatClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_web_view_demo),
            onClick = {
                onAction(MainScreenAction.OnWebViewClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_video_play_demo),
            onClick = onVideoPlayClick,
        )
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeTitle(title = stringResource(id = R.string.string_demo_group_authentication))
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_biometric),
            onClick = onBiometricClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_google_login_demo),
            onClick = {
                GoogleAuthenticate().requestLogin(
                    activityContext = context,
                    onSuccess = { email, idToken ->
                        Toast.showText("$email $idToken")
                    },
                    onFail = {
                        Toast.showText("...")
                    })
            }
        )
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeTitle(title = stringResource(id = R.string.string_demo_group_device_system))
        WeOutline(weOutlineType = WeOutlineType.Full)

        WeSwitch(
            title = stringResource(id = R.string.string_demo_screen_switch_app_logo),
            checked = uiState.enableSwitchAppLogo,
            onCheckedChange = {
                onSwitchAppLogoClick()
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_get_location),
            summary = uiState.location,
            onClick = onLocationClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)


        WeClick(
            title = stringResource(id = R.string.string_demo_screen_select_mobile_demo),
            onClick = onSelectMobileClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_open_album),
            onClick = onOpenAlbumClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)


        WeClick(
            title = stringResource(id = R.string.string_demo_screen_open_camera),
            onClick = onOpenCameraClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)

        WeClick(
            title = stringResource(id = R.string.string_demo_screen_open_contact),
            onClick = onOpenContactClick,
        )



        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}

@Preview
@Composable
private fun PreviewFeatureScreen() {
    QuicklyTheme {
        WeScaffold {
            FeatureScreenUi(
                uiState = FeatureScreenUiState(),
                onSwitchAppLogoClick = {},
                onLocationClick = {},
                onSelectMobileClick = {},
                onOpenAlbumClick = {},
                onOpenCameraClick = {},
                onOpenContactClick = {},
                onVideoPlayClick = {},
                onBiometricClick = {},
                onAction = {})
        }
    }
}
