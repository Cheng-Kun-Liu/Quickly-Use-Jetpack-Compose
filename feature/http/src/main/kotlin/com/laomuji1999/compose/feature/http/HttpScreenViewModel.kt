package com.laomuji1999.compose.feature.http

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.model.HttpResult
import com.laomuji1999.compose.core.logic.model.request.CreateUserRequest
import com.laomuji1999.compose.core.logic.repository.user.UserRepository
import com.laomuji1999.compose.core.ui.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HttpScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isError = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _responseText = MutableStateFlow("")

    val uiState = combine(
        _isError,
        _isLoading,
        userRepository.isConnectedFlow,
        _responseText
    ) { isError, isLoading, isConnect, responseText ->
        HttpScreenUiState(
            isError = isError,
            isLoading = isLoading,
            isConnect = isConnect,
            responseText = responseText
        )
    }.stateInTimeout(viewModelScope, HttpScreenUiState())

    fun onAction(action: HttpScreenAction) {
        when (action) {
            HttpScreenAction.GetListUsers -> getListUsers()
            HttpScreenAction.CreateUser -> createUser()
        }
    }

    private fun getListUsers() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            userRepository.delayRequest().collect {
                when (it) {
                    is HttpResult.Error -> {
                        _isLoading.value = false
                        _isError.value = true
                    }

                    HttpResult.Loading -> {
                        _isLoading.value = true
                    }

                    is HttpResult.Success -> {
                        _isLoading.value = false
                        _responseText.value = it.data
                    }
                }
            }
        }
    }

    private fun createUser() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            userRepository.createUser(
                CreateUserRequest(
                    name = "ZhangSan",
                    job = "android"
                )
            ).collect {
                when (it) {
                    is HttpResult.Error -> {
                        Log.d("tag_http_net", "Error")
                        _isLoading.value = false
                        _isError.value = true
                    }

                    HttpResult.Loading -> {
                        Log.d("tag_http_net", "Loading")
                        _isLoading.value = true
                    }

                    is HttpResult.Success -> {
                        Log.d("tag_http_net", "Success")
                        _isLoading.value = false
                        _responseText.value = it.data
                    }
                }
            }
        }
    }
}