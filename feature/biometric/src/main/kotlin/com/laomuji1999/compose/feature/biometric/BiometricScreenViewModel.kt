package com.laomuji1999.compose.feature.biometric

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.authenticate.biometric.BiometricAuthenticate
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class BiometricScreenViewModel @Inject constructor(
    private val biometricAuthenticate: BiometricAuthenticate
) : ViewModel() {
    private val _title = MutableStateFlow("")
    private val _description = MutableStateFlow("")

    val uiState = combine(
        _title,
        _description,
        biometricAuthenticate.resultFlow
    ){ title, description, biometricResult ->
        BiometricScreenUiState(
            title = title,
            description = description,
            biometricResult = biometricResult
        )
    }.stateInTimeout(viewModelScope, BiometricScreenUiState())

    fun onAction(action: BiometricScreenAction){
        when(action){
            is BiometricScreenAction.HandleBiometric -> handleBiometric(action.activityContext)
            is BiometricScreenAction.OnSettingClick -> getBiometricSettingIntent().let {
                action.biometricLauncher.launch(it)
            }
            is BiometricScreenAction.OnTitleChange -> setTitle(action.title)
            is BiometricScreenAction.OnDescriptionChange -> setDescription(action.description)
        }
    }

    private fun handleBiometric(context: Context){
        if(_title.value.isEmpty()){
            //标题不能为空
            return
        }
        biometricAuthenticate.handleBiometric(context, _title.value, _description.value)
    }

    private fun getBiometricSettingIntent() = biometricAuthenticate.getBiometricSettingIntent()

    private fun setTitle(title: String){
        _title.value = title
    }

    private fun setDescription(description: String){
        _description.value = description
    }
}