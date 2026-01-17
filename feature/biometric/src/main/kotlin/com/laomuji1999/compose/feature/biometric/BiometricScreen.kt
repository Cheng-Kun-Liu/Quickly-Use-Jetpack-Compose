package com.laomuji1999.compose.feature.biometric

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.logic.authenticate.biometric.BiometricAuthenticate
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.input.WeInput
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.res.R.string

@Composable
fun BiometricScreen(
    viewModel: BiometricScreenViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val biometricLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    BiometricScreenUi(
        uiState = uiState,
        onAction = viewModel::onAction,
        biometricLauncher = biometricLauncher
    )
}

@Composable
private fun BiometricScreenUi(
    uiState: BiometricScreenUiState,
    onAction: (BiometricScreenAction) -> Unit,
    context: Context = LocalContext.current,
    biometricLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?=null
){
    val text = when(uiState.biometricResult){
        BiometricAuthenticate.BiometricAuthenticateResult.HardwareNotFound -> stringResource(id = string.string_biometric_screen_status_hardware_not_found)
        BiometricAuthenticate.BiometricAuthenticateResult.HardwareUnavailable -> stringResource(id = string.string_biometric_screen_status_hardware_unavailable)
        BiometricAuthenticate.BiometricAuthenticateResult.NotSetBiometric -> stringResource(id = string.string_biometric_screen_status_hardware_not_set)
        is BiometricAuthenticate.BiometricAuthenticateResult.OtherError -> stringResource(id = string.string_biometric_screen_status_hardware_other_error, uiState.biometricResult.msg)
        BiometricAuthenticate.BiometricAuthenticateResult.Success -> stringResource(id = string.string_biometric_screen_status_hardware_success)
        BiometricAuthenticate.BiometricAuthenticateResult.Fail -> stringResource(id = string.string_biometric_screen_status_hardware_fail)
        null -> { "" }
    }
    WeScaffold(
        topBar = {
            WeTopBar(title = text)
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(10.dp))
            WeInput(
                title = stringResource(id = string.string_biometric_screen_title_title),
                tip = stringResource(id = string.string_biometric_screen_title_tip),
                value = uiState.title,
                onValueChange = {onAction(BiometricScreenAction.OnTitleChange(it))}
            )
            Spacer(modifier = Modifier.height(10.dp))
            WeInput(
                title = stringResource(id = string.string_biometric_screen_description_title),
                tip = stringResource(id = string.string_biometric_screen_description_tip),
                value = uiState.description,
                onValueChange = {onAction(BiometricScreenAction.OnDescriptionChange(it))}
            )
            Spacer(modifier = Modifier.height(20.dp))

            WeButton(
                text = stringResource(id = string.string_biometric_screen_handle_title),
                onClick = { onAction(BiometricScreenAction.HandleBiometric(context)) },
                weButtonType = WeButtonType.Big
            )

            if(uiState.biometricResult is BiometricAuthenticate.BiometricAuthenticateResult.NotSetBiometric){
                Spacer(modifier = Modifier.height(20.dp))
                WeButton(
                    text = stringResource(id = string.string_biometric_screen_setting_title),
                    onClick = { onAction(BiometricScreenAction.OnSettingClick(biometricLauncher!!)) },
                    weButtonType = WeButtonType.Big,
                    weButtonColor = WeButtonColor.Secondary
                )
            }
        }

    }
}

@Preview
@Composable
private fun PreviewBiometricScreenUi() {
    QuicklyTheme {
        BiometricScreenUi(
            uiState = BiometricScreenUiState(),
            onAction = {}
        )
    }
}