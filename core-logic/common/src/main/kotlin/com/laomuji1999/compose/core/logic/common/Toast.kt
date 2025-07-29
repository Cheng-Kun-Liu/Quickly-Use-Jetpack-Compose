package com.laomuji1999.compose.core.logic.common

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object Toast {
    val toastShardFlow = MutableSharedFlow<String>()

    fun showText(text: CharSequence?) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                toastShardFlow.emit("$text")
                delay(2000)
                toastShardFlow.emit("")
            }
        }
    }

    fun showText(context: Context, resId: Int) {
        showText(context.resources.getText(resId))
    }
}
