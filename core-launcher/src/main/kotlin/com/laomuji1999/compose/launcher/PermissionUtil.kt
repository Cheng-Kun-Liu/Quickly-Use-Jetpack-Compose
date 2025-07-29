package com.laomuji1999.compose.launcher

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.res.R

object PermissionUtil {
    private const val TAG = "tag_permission"

    /**
     * 获取权限回调
     * @param permissions 权限列表
     * @param onCallback 回调
     */
    @Composable
    fun getPermissionsLauncher(
        permissions: List<String>,
        onCallback: (granted: List<String>, denied: List<String>, foreverDenied: List<String>) -> Unit
    ): () -> Unit {
        val activity = LocalContext.current as Activity
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { isGrantedMap ->
            val grantedList = mutableListOf<String>()
            val deniedList = mutableListOf<String>()
            val foreverDeniedList = mutableListOf<String>()
            isGrantedMap.forEach {
                if (it.value) {
                    Log.debug(TAG, "${it.key} granted")
                    grantedList.add(it.key)
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it.key)) {
                        Log.debug(TAG, "${it.key} denied")
                        deniedList.add(it.key)
                    } else {
                        Log.debug(TAG, "${it.key} forever denied")
                        foreverDeniedList.add(it.key)
                    }
                }
            }
            onCallback(
                grantedList, deniedList, foreverDeniedList
            )
        }
        return {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    /**
     * 获取权限回调
     * @param permission 权限
     * @param context 上下文, 用来跳转Setting
     * @param onDenied 权限被拒绝
     * @param onForeverDenied 权限被永久拒绝
     * @param onGranted 权限被授权
     */
    @Composable
    fun getPermissionLauncher(
        permission: String,
        context: Context = LocalContext.current,
        onDenied: (context: Context) -> Unit = { Setting.openSetting(context) },
        onForeverDenied: (context: Context) -> Unit = onDenied,
        onGranted: () -> Unit,
    ): () -> Unit {
        return getPermissionsLauncher(
            permissions = listOf(permission), onCallback = { _, denied, foreverDenied ->
                if (foreverDenied.isNotEmpty()) {
                    onForeverDenied(context)
                } else if (denied.isNotEmpty()) {
                    onDenied(context)
                } else {
                    onGranted()
                }
            })
    }

    /**
     * 检查权限是否被授予
     * @param context 上下文
     * @param permission 权限
     */
    fun hasPermission(
        context: Context, permission: String
    ) = ContextCompat.checkSelfPermission(
        context, permission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    /**
     * 检查权限是否被授予
     */
    fun hasPermissions(
        context: Context, permissions: List<String>
    ): Boolean {
        for (permission in permissions) {
            if (!hasPermission(context, permission)) {
                return false
            }
        }
        return true
    }

    object Setting {
        fun openSetting(context: Context) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun openNotificationSettings(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)
            }
        }

        fun openGpsSetting(context: Context) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    /**
     * 获取通知权限回调
     * 通知权限只有允许和永久拒绝
     * @param context 上下文
     * @param onDenied 权限被拒绝
     * @param onGranted 权限被授权
     */
    @Composable
    fun getPostNotificationLauncher(
        context: Context = LocalContext.current,
        onDenied: (context: Context) -> Unit = {},
        onForeverDenied: (context: Context) -> Unit = {
            Toast.showText(
                context = context, resId = R.string.string_permission_notification_forever_denied
            )
            Setting.openNotificationSettings(context)
        },
        onGranted: () -> Unit
    ): () -> Unit {
        val permissionLauncher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPermissionLauncher(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                onDenied = onDenied,
                onForeverDenied = onForeverDenied,
                onGranted = onGranted
            )
        } else onGranted
        return permissionLauncher
    }

    /**
     * 是否拥有通知权限
     * @param context 上下文
     */
    fun hasPostNotificationPermission(
        context: Context
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return hasPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        }
        return true
    }
}