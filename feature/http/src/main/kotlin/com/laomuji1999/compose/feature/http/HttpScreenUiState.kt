package com.laomuji1999.compose.feature.http

import com.laomuji1999.compose.core.logic.model.response.ProductResponse

data class HttpScreenUiState(
    val isError:Boolean = false,
    val isLoading:Boolean = false,
    val isConnect:Boolean = false,
    val responseText:String = "",
    val products: List<ProductResponse.Product> = emptyList(),
    val isLoadMore:Boolean = false,
)
