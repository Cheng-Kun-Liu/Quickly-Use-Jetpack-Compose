package com.laomuji1999.compose.feature.main.feature

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.logic.authenticate.GoogleAuthenticate
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.view.LoadingDialog
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.switc.WeSwitch
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
        onAction = onAction,
    )
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
    onAction: (MainScreenAction) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
            title = stringResource(id = R.string.string_demo_screen_biometric),
            onClick = {
                onAction(MainScreenAction.OnBiometricScreenClick)
            },
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
                onAction = {})
        }
    }
}