package net.manyisoft.library.stroll.core

/**
 * 上传数据核心类
 */

interface Upload{

    /**
     * 设置基础url
     */
    fun setBaseUrl(baseUrl: String): Upload

    /**
     * 设置访问接口
     */
    fun setPath(path: String): Upload

    /**
     * 批量设置请求头
     */
    fun setHeader(key: String,value: String): Upload

    /**
     * 添加请求头
     *
     */
    fun addHeaders(headers: HashMap<String,String> = HashMap()): Upload

    /**
     * 给当前请求添加标识
     */
    fun setTag(tag: String): Upload

    /**
     * 添加网络读取数据超时时间 单位： s
     */
    fun setReadTimeOut(timeOut: Int): Upload

    /**
     * 添加网络读取数据超时时间 单位： s
     */
    fun setWriteTimeOut(timeOut: Int): Upload

    /**
     * 添加网络连接超时时间 单位： s
     */
    fun setConnectTimeOut(timeOut: Int): Upload


    /**
     * 开始请求数据
     *
     */
    fun build()


}


