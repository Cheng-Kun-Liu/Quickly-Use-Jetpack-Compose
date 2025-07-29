package com.laomuji1999.compose.core.logic.repository.contacts

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.webkit.MimeTypeMap

class ContactAssetFileProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        return true
    }

    override fun getType(uri: Uri): String? {
        val segments = uri.pathSegments
        return when (segments[0]) {
            "avatar" -> MimeTypeMap.getSingleton().getMimeTypeFromExtension("png")
            else -> "application/octet-stream"
        }
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val segments = uri.pathSegments
        return when (segments[0]) {
            "avatar" -> {
                context?.resources?.assets?.openFd(segments[1])
            }
            else -> null
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

}