package com.laomuji1999.compose.launcher

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.laomuji1999.compose.core.ui.extension.isPreview


object OpenContact {

    data class SelectContactInfo(
        val name: String,
        val mobile: String,
    )

    /**
     * 选择联系人回调, 为空表示取消
     */
    data class OpenContactLauncher(
        val launch: (onResult: (SelectContactInfo?) -> Unit) -> Unit,
    )

    /**
     * 选择联系人
     * 不需要权限
     * 姓名可能为空
     */
    @Composable
    fun openContact(): OpenContactLauncher {
        if (isPreview()) {
            return OpenContactLauncher { _ -> }
        }

        val context = LocalContext.current
        var resultCallback by remember {
            mutableStateOf<((SelectContactInfo?) -> Unit)?>(null)
        }

        val contactPickLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                val callback = resultCallback
                resultCallback = null

                if (result.resultCode != Activity.RESULT_OK) {
                    callback?.invoke(null)
                    return@rememberLauncherForActivityResult
                }

                val uri = result.data?.data
                if (uri == null) {
                    callback?.invoke(null)
                    return@rememberLauncherForActivityResult
                }

                try {
                    callback?.invoke(
                        getContactInfo(
                            context = context,
                            uri = uri
                        )
                    )
                } catch (_: Exception) {
                    callback?.invoke(null)
                }
            }
        )

        return remember {
            OpenContactLauncher { onResult ->
                resultCallback = onResult
                try {
                    contactPickLauncher.launch(
                        Intent(Intent.ACTION_PICK).apply {
                            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                        }
                    )
                } catch (_: Exception) {
                    onResult(null)
                    resultCallback = null
                }
            }
        }
    }

    private fun getContactInfo(context: Context, uri: Uri): SelectContactInfo {
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null) ?: return SelectContactInfo("","")
        cursor.moveToFirst()
        val namePosition = cursor.getColumnIndex("display_name")
        val mobilePosition = cursor.getColumnIndex("data1")
        val selectContactInfo = SelectContactInfo(
            name = cursor.getString(namePosition)?:"",
            mobile = cursor.getString(mobilePosition)
        )
        cursor.close()
        return selectContactInfo
    }
}