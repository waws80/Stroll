package net.manyisoft.library.stroll.datacache

import java.io.InputStream


/**
 * 缓存实体类
 */
data class CacheEntry(var currentTime: Long = System.currentTimeMillis(), var value: InputStream)