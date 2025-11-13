package com.laomuji1999.compose

import android.app.ActivityManager
import android.os.Bundle
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.ui.screen.SlideActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作为从其他app回来的中间层,回到之前的页面状态.
 */
@AndroidEntryPoint
class SchemeActivity : SlideActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.debug("tag_scheme", "callback:${packageName}")
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val myTask = am.appTasks.firstOrNull()
        if (myTask != null) {
            myTask.moveToFront()
        } else {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            startActivity(intent)
        }
        finish()
    }
}