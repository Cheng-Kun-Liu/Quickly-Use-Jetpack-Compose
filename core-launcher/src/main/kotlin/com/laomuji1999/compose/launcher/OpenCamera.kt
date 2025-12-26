package com.laomuji1999.compose.launcher

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.laomuji1999.compose.core.ui.isPreview
import java.io.File

object OpenCamera {
    /**
     * 相机拍照回调,为空表示取消选择.
     */
    data class OpenAlbumLauncher(
        val launch: (onResult: (Uri?) -> Unit) -> Unit,
    )

    /**
     * 打开相机
     * 在AndroidManifest.xml里
     * 写了相机权限,就必须有相机权限才能打开,否则异常.
     * 没有写相机权限,可以直接打开,不会异常.
     */
    @Composable
    fun openCamera(): OpenAlbumLauncher {
        if (isPreview()) {
            return OpenAlbumLauncher { _ -> }
        }

        val context = LocalContext.current
        var resultCallback by remember { mutableStateOf<((Uri?) -> Unit)?>(null) }
        var photoUri by remember { mutableStateOf<Uri?>(null) }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    resultCallback?.invoke(photoUri)
                } else {
                    resultCallback?.invoke(null)
                }
                resultCallback = null
                photoUri = null
            }
        )

        return remember {
            OpenAlbumLauncher { onResult ->
                resultCallback = onResult
                try {
                    photoUri = context.createTempPictureUri()
                    if (photoUri == null) {
                        onResult(null)
                        resultCallback = null
                        return@OpenAlbumLauncher
                    }
                    cameraLauncher.launch(photoUri)
                } catch (_: Exception) {
                    onResult(null)
                    resultCallback = null
                }
            }
        }
    }

    private fun Context.createTempPictureUri(
        provider: String = "${applicationContext.packageName}.provider",
        filename: String = "${System.currentTimeMillis()}"
    ): Uri? {
        val tempFile = File(externalCacheDir, filename).apply {
            createNewFile()
        }
        return FileProvider.getUriForFile(applicationContext, provider, tempFile)
    }
}