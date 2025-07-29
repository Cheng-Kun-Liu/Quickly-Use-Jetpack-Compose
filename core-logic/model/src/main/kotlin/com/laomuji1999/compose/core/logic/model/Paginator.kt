package com.laomuji1999.compose.core.logic.model

/**
 * @param initialKey 初始的key,第一页
 * @param onRequest 请求数据的回调
 * @param onLoadStatusChanged 加载状态的回调,在执行[onRequest]前会调用.
 * @param getNextKey 获取下一页的key的回调,在执行完[onRequest]后调用.
 * @param onError 错误的回调,在执行[onRequest]失败后调用.
 * @param onSuccess 成功回调,在执行[onRequest]成功后调用.
 * @param onRequestEnd 请求结束的回调,用来更新是否还有下一页.
 */
class Paginator<Key, Value>(
    private val initialKey: Key,
    private val onRequest: suspend (key: Key) -> Result<Value>,
    private val onLoadStatusChanged: (isLoading: Boolean) -> Unit,
    private val getNextKey: suspend (value: Value) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (value: Value) -> Unit,
    private val onRequestEnd: suspend (value: Value, currentKey: Key) -> Boolean
) {
    //当前key
    private var currentKey = initialKey

    //是否正在请求中
    private var isInRequest = false

    //是否没有下一页了
    private var isKeyEnd = false

    /**
     * 刷新
     */
    fun refresh(){
        currentKey = initialKey
        isKeyEnd = false
    }

    /**
     * 加载下一页
     */
    suspend fun loadNextPage() {
        //正在请求中或者没有下一页了,返回
        if (isInRequest || isKeyEnd) {
            return
        }

        //请求开始
        isInRequest = true
        onLoadStatusChanged(true)
        val result = onRequest(currentKey)
        isInRequest = false

        //请求异常,调用失败.
        val value = result.getOrElse {
            onError(it)
            onLoadStatusChanged(false)
            return
        }

        //请求成功
        currentKey = getNextKey(value)
        onSuccess(value)
        onLoadStatusChanged(false)

        //请求结束,询问是否还有下一页
        isKeyEnd = onRequestEnd(value, currentKey)
    }
}