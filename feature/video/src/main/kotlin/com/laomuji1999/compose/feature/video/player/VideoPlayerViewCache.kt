package com.laomuji1999.compose.feature.video.player

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

internal object VideoPlayerViewCache {
    /**
     * 最大缓存大小
     * 2048MB
     */
    private const val MAX_CACHE_SIZE = 2048L * 1024L * 1024L

    lateinit var cache: SimpleCache
        private set

    fun init(context: Context) {
        if (::cache.isInitialized) return
        val cacheDir = File(context.cacheDir, "VideoPlayerViewCache")
        val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)
        cache = SimpleCache(cacheDir, evictor, StandaloneDatabaseProvider(context))
    }

    fun createMediaSourceFactory(): CacheDataSource.Factory {
        return CacheDataSource.Factory().setCache(cache)
            .setUpstreamDataSourceFactory(
                DefaultHttpDataSource.Factory()
            ).setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}