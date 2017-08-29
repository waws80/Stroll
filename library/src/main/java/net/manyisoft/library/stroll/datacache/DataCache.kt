package net.manyisoft.library.stroll.datacache

import java.io.InputStream

/**
 * 数据缓存
 */
internal interface DataCache {

    fun getId(): String

    fun get(key: String): Any?

    fun put(key: String,value: InputStream)

    fun remove(key: String)

    fun clear()

    fun size(): Int
}