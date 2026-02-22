package com.laomuji1999.compose.feature.http

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.core.logic.model.HttpResult
import com.laomuji1999.compose.core.logic.model.Paginator
import com.laomuji1999.compose.core.logic.model.request.CreateUserRequest
import com.laomuji1999.compose.core.logic.model.response.ProductResponse
import com.laomuji1999.compose.core.logic.repository.product.ProductRepository
import com.laomuji1999.compose.core.logic.repository.user.UserRepository
import com.laomuji1999.compose.core.ui.extension.combine
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HttpScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val _isError = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _responseText = MutableStateFlow("")

    private var currentPage = 0
    private val _products = MutableStateFlow<List<ProductResponse.Product>>(emptyList())
    private val _isLoadMore = MutableStateFlow(false)

    private val paginator = Paginator(
        initialKey = currentPage,
        onRequest = { key ->
            productRepository.getProducts(key)
        },
        onLoadStatusChanged = { loading ->
            _isLoadMore.update {
                loading
            }
        },
        getNextKey = { value ->
            //当前页可能是value携带的,也可能是页面数量+1,这里假设直接页面数量+1
            ++currentPage
        },
        onError = { e ->
            Toast.showText(e?.message)
        },
        onSuccess = {
            _products.value = _products.value + it.products
        },
        onRequestEnd = { value, key ->
            _products.value.size >= value.total || key < 0
        }
    )

    val uiState = combine(
        _isError,
        _isLoading,
        userRepository.isConnectedFlow,
        _responseText,
        _products,
        _isLoadMore,
    ) { isError, isLoading, isConnect, responseText, products, isLoadMore ->
        HttpScreenUiState(
            isError = isError,
            isLoading = isLoading,
            isConnect = isConnect,
            responseText = responseText,
            products = products,
            isLoadMore = isLoadMore,
        )
    }.stateInTimeout(viewModelScope, HttpScreenUiState())

    init {
        onAction(HttpScreenAction.LoadMore)
    }

    fun onAction(action: HttpScreenAction) {
        when (action) {
            HttpScreenAction.GetListUsers -> getListUsers()
            HttpScreenAction.CreateUser -> createUser()
            HttpScreenAction.LoadMore -> viewModelScope.launch {
                paginator.loadNextPage()
            }
        }
    }

    private fun getListUsers() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch {
            userRepository.delayRequest().collect {
                when (it) {
                    HttpResult.Loading -> {
                        _isLoading.value = true
                    }

                    is HttpResult.Error -> {
                        _isLoading.value = false
                        _isError.value = true
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
                    job = "Android Developer"
                )
            ).collect {
                when (it) {
                    HttpResult.Loading -> {
                        _isLoading.value = true
                    }

                    is HttpResult.Error -> {
                        _isLoading.value = false
                        _isError.value = true
                    }

                    is HttpResult.Success -> {
                        _isLoading.value = false
                        _responseText.value = it.data
                    }
                }
            }
        }
    }
}