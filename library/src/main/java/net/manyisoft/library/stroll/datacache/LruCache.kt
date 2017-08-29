package net.manyisoft.library.stroll.datacache

import net.manyisoft.library.stroll.util.StrollLog
import java.io.InputStream
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * 缓存
 */
class LruCache(private val id: String = UUID.randomUUID().toString(),
               private val maxSize: Int = 10,
               private val timeOut: Long = 0L) : DataCache {


    private val cache: LinkedHashMap<String, CacheEntry> = LruMap(maxSize)
    override fun getId() = this.id


    override fun get(key: String): Any? {
       StrollLog.msg("获取缓存：")
        cache.forEach { StrollLog.msg("键：${it.key}    值：${it.value}") }
        var value: Any? = null
        val entry: CacheEntry?
        @Synchronized
        entry = cache[key]
        if (entry != null){
            if (System.currentTimeMillis() - entry.currentTime < timeOut || timeOut == 0L){
                value = entry.value
            }else{
                cache.remove(key)
            }
        }
        return value
    }

    @Synchronized
    override fun put(key: String, value: InputStream) {
        StrollLog.msg("添加缓存：")
        cache.put(key, CacheEntry(System.currentTimeMillis(),value))
        cache.forEach { StrollLog.msg("键：${it.key}    值：${it.value}") }
    }

    @Synchronized
    override fun remove(key: String) {
        cache.remove(key)
    }

    @Synchronized
    override fun clear() {
        cache.clear()
    }

    override fun size()  = cache.size
}