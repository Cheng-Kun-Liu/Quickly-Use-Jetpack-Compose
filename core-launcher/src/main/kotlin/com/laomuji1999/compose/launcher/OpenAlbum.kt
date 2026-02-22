package com.laomuji1999.compose.launcher

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.laomuji1999.compose.core.ui.extension.isPreview

object OpenAlbum {
    /**
     * 打开相册回调,为空表示取消选择.
     */
    data class OpenAlbumLauncher(
        val launch: (onResult: (Uri?) -> Unit) -> Unit,
    )

    /**
     * 打开相册,兼容新老版本.
     */
    @Composable
    fun openAlbum(): OpenAlbumLauncher {
        if (isPreview()) {
            return OpenAlbumLauncher { _ -> }
        }

        var resultCallback by remember { mutableStateOf<((Uri?) -> Unit)?>(null) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val pickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri ->
                    resultCallback?.invoke(uri)
                    resultCallback = null
                },
            )
            return remember {
                val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                OpenAlbumLauncher { onResult ->
                    resultCallback = onResult
                    try {
                        pickerLauncher.launch(request)
                    } catch (_: Exception) {
                        onResult(null)
                        resultCallback = null
                    }
                }
            }
        } else {
            val oldPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    val uri = result.data?.data
                    resultCallback?.invoke(uri)
                    resultCallback = null
                }
            )
            return remember {
                OpenAlbumLauncher { onResult ->
                    resultCallback = onResult
                    try {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        oldPicker.launch(intent)
                    } catch (_: Exception) {
                        onResult(null)
                        resultCallback = null
                    }
                }
            }
        }
    }
}