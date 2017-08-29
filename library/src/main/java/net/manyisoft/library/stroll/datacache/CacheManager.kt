package net.manyisoft.library.stroll.datacache

/**
 * 缓存管理器
 */
class CacheManager(private  val cache: LruCache) {



    companion object {
        /**
         *默认缓存存在5分钟
         */
        fun creatCache(timeOut: Long = 1000*60*5) = CacheManager(LruCache(timeOut = timeOut))
    }

    fun add(key: String , entry: CacheEntry){
        cache.put(key,entry.value)
    }

    fun get(key: String) = cache.get(key)

    fun remove(key: String){
        cache.remove(key)
    }

    fun clear(){
        cache.clear()
    }

    fun size() = cache.size()
}