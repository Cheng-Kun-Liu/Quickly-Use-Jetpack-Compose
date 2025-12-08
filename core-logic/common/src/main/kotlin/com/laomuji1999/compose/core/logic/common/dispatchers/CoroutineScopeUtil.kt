package com.laomuji1999.compose.core.logic.common.dispatchers

import com.laomuji1999.compose.core.logic.common.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object CoroutineScopeUtil {
    private val handler = CoroutineExceptionHandler { _, e ->
        Log.debug("tag_CoroutineScopeUtil", "Exception: ${e.message}")
    }

    fun ioScope() = CoroutineScope(Dispatchers.IO + SupervisorJob() + handler)
    fun mainScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main + handler)
}