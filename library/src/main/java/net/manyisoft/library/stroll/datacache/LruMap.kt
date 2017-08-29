package net.manyisoft.library.stroll.datacache

import java.util.LinkedHashMap

/**
 * @author liuxiongfei
 */
internal class LruMap(private val maxSize: Int) : LinkedHashMap<String, CacheEntry>(maxSize / 2 + 1, 0.75f, true) {

    /**
     * 当缓存数量超过最大容量时候，返回true移除最后一个
     */
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CacheEntry>?) =
            size > maxSize

}
