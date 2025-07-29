package com.laomuji1999.compose.core.logic.model

/**
 * 简单处理一下Result
 */
suspend fun <T> resultSuccessOrFailure(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}