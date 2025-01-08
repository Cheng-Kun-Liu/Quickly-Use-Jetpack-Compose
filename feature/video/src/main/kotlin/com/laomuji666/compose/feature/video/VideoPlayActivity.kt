package com.laomuji666.compose.feature.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.laomuji666.compose.core.ui.theme.QuicklyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class VideoPlayActivity : AppCompatActivity() {
    companion object{
        private const val VIDEO_URI = "VIDEO_URI"
        fun open(context: Context, videoUri: String){
            val intent = Intent(context, VideoPlayActivity::class.java)
            intent.putExtra(VIDEO_URI, videoUri)
            context.startActivity(intent)
        }

        fun openVideoOtherApp(context: Context, filename:String){
            val uri = filename.runCatching {
                DocumentFile.fromSingleUri(context, Uri.parse(filename)).run {
                    if (this?.exists() == true) {
                        this.uri
                    } else if (File(this@runCatching).exists()) {
                        FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            File(this@runCatching),
                        )
                    } else null
                }
            }.getOrNull() ?: return

            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                data = uri
                setDataAndType(this.data, data?.let { context.contentResolver.getType(it) } ?: "media/*")

                putExtra(Intent.EXTRA_STREAM, data)
            }
            context.startActivity(intent)
        }
    }

    private val videoUri: String by lazy {
        intent.getStringExtra(VIDEO_URI) ?: ""
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            QuicklyTheme {
                VideoScreen(videoUri = videoUri)
            }
        }
    }
}