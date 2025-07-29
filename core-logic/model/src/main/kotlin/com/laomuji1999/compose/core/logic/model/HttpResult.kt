package com.laomuji1999.compose.core.logic.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * 统一http接口返回状态
 */
sealed interface HttpResult<out T> {
    data class Success<T>(val data: T) : HttpResult<T>
    data class Error(val exception: Throwable) : HttpResult<Nothing>
    data object Loading : HttpResult<Nothing>
}

/**
 * 这里的catch 也可以捕获 http响应码, 比如401就退出登录.
 * 需要自己额外判断.
 */
fun <T> Flow<T>.asHttpResult(): Flow<HttpResult<T>> = map<T, HttpResult<T>> { HttpResult.Success(it) }
    .onStart { emit(HttpResult.Loading) }
    .catch { emit(HttpResult.Error(it)) }
