package com.laomuji1999.compose.core.ui.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.laomuji1999.compose.core.logic.common.dispatchers.CoroutineScopeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 禁止截屏
 */
@Composable
fun SecureComposeView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val activity = context.findActivity()
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }

                Lifecycle.Event.ON_PAUSE -> {
                    val job = CoroutineScopeUtil.mainScope().launch {
                        delay(300)
                        if (!lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                        }
                    }

                    onDispose { job.cancel() }
                }


                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}