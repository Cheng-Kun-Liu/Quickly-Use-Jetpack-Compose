package com.laomuji1999.compose

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.laomuji1999.compose.core.logic.Language
import com.laomuji1999.compose.core.ui.we.cache.WeCache
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * [HiltAndroidApp] hilt依赖注入入口
 * [newImageLoader] coil缓存图片的规则
 */
@HiltAndroidApp
class ComposeApp : Application(), ImageLoaderFactory {

    @Inject
    lateinit var language: Language

    /**
     * coil缓存图片的规则
     * 启用磁盘缓存,保存到临时目录的image_coil文件夹下,在无网状态下也能使用
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder().diskCachePolicy(CachePolicy.ENABLED).diskCache {
            DiskCache.Builder().maxSizePercent(0.03).directory(cacheDir.resolve("image_coil"))
                .build()
        }.respectCacheHeaders(false).logger(DebugLogger()).build()
    }

    override fun onCreate() {
        super.onCreate()
        language.initLanguage(this)

        WeCache.initWeThemeCache(this)
    }
}