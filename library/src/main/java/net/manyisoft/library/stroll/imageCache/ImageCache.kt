package net.manyisoft.library.stroll.imageCache

import android.graphics.Bitmap

/**
 * 缓存接口
 */

 interface ImageCache {

    fun getCache(path: String): Bitmap?
    fun putCache(path: String, bitmap: Bitmap)
}
