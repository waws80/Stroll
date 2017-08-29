package net.manyisoft.library.stroll.imageCache

import android.graphics.Bitmap
import android.util.LruCache
import net.manyisoft.library.stroll.util.StrollLog

/**
 * 内存缓存
 */

 class MemeryCache: ImageCache {


    /**
     * 取内存的四分之一作为缓存
     */
    private val cacheSize= (Runtime.getRuntime().maxMemory()/1024/4).toInt()

    private val mCache: LruCache<String, Bitmap> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        object: LruCache<String, Bitmap>(cacheSize){
            override fun sizeOf(key: String?, value: Bitmap?): Int =
                    value!!.rowBytes*value.height
        }
    }


    override fun getCache(path: String): Bitmap? {
        StrollLog.msg("getCache: find image in $path memerycache")
        return  mCache.get(path)
    }

    override fun putCache(path: String, bitmap: Bitmap) {
        mCache.put(path,bitmap)
    }
}
