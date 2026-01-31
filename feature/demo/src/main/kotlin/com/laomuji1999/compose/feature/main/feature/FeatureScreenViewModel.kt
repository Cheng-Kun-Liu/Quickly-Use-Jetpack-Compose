package com.laomuji1999.compose.feature.main.feature

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.common.cache.CacheUtil
import com.laomuji1999.compose.core.logic.location.Locator
import com.laomuji1999.compose.core.logic.location.LocatorResult
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import com.laomuji1999.compose.launcher.PermissionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeatureScreenViewModel @Inject constructor(
    cacheUtil: CacheUtil, private val locator: Locator
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private var enableSwitchAppLogo by cacheUtil.cacheable("enableSwitchAppLogo", false)
    private val _enableSwitchAppLogo = MutableStateFlow(enableSwitchAppLogo)
    private val _location = MutableStateFlow("")

    val uiState = combine(
        _isLoading,
        _enableSwitchAppLogo,
        _location,
    ) { isLoading, enableSwitchAppLogo, location ->
        FeatureScreenUiState(
            isLoading = isLoading, enableSwitchAppLogo = enableSwitchAppLogo, location = location
        )
    }.stateInTimeout(viewModelScope, FeatureScreenUiState())

    fun switchAppLogo(context: Context) {
        val targetValue = !_enableSwitchAppLogo.value
        _enableSwitchAppLogo.value = targetValue
        enableSwitchAppLogo = targetValue
        switchAliasActivity(context, targetValue)
    }

    private fun switchAliasActivity(context: Context, isDynamicAlias: Boolean) {
        val defaultAliasCls = "com.laomuji1999.compose.DefaultAliasActivity"
        val dynamicAliasCls = "com.laomuji1999.compose.DynamicAliasActivity"

        val enableAliasActivity =
            ComponentName(context, if (isDynamicAlias) dynamicAliasCls else defaultAliasCls)
        context.packageManager.setComponentEnabledSetting(
            enableAliasActivity,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        val disableAliasActivity =
            ComponentName(context, if (isDynamicAlias) defaultAliasCls else dynamicAliasCls)
        context.packageManager.setComponentEnabledSetting(
            disableAliasActivity,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun getLocation(context: Context, onPermissionDenied: () -> Unit) {
        viewModelScope.launch {
            _isLoading.update {
                true
            }
            when (val currentLocation = locator.getCurrentLocation()) {
                is LocatorResult.Success -> {
                    _location.update {
                        "${currentLocation.location.latitude}\n${currentLocation.location.longitude}"
                    }
                }

                is LocatorResult.Error -> {

                    _location.update {
                        "Error"
                    }
                }

                LocatorResult.Cancelled -> {
                    _location.update {
                        "Cancelled"
                    }
                }

                LocatorResult.PermissionDenied -> {
                    _location.update {
                        "PermissionDenied"
                    }
                    onPermissionDenied()
                }

                LocatorResult.ProvidersDisabled -> {
                    _location.update {
                        "ProvidersDisabled"
                    }
                    PermissionUtil.Setting.openGpsSetting(context)
                }

            }
            _isLoading.update {
                false
            }
        }
    }
}