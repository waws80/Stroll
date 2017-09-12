package net.manyisoft.library.stroll

import android.support.annotation.DrawableRes
import android.view.View
import net.manyisoft.library.stroll.core.*
import net.manyisoft.library.stroll.img.ImageListener
import net.manyisoft.library.stroll.threadpool.ThreadPool
import java.nio.charset.Charset

/**
 * 网络访问框架
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 * Stroll 意为：漫步
 */

private var method: String = "GET"

private var mConfig: StrollConfig = StrollConfig()

private var isInstall = false

private val threadPool = ThreadPool()

class Stroll private constructor(){


    companion object {

        private val INSTANCE: Stroll by lazy { Stroll() }

        var config: StrollConfig? = null

        /**
         * 全局初始化网络访问框架
         */
        fun install(config: StrollConfig = StrollConfig()): Stroll {
            mConfig = config
            this.config = mConfig
            isInstall = true
            return INSTANCE
        }

        /**
         *get请求
         */
        fun get(): Stroll{
            installException()
            method = "GET"
            return INSTANCE
        }

        /**
         *post请求
         */
        fun post(): Stroll{
            installException()
            method = "POST"
            return INSTANCE
        }


        /**
         *上传data
         */
        fun uploadData(): DataUpload {
            installException()
            method = "POST"
            return DataUpload(mConfig, threadPool)
        }

        /**
         *上传表单
         */
        fun uploadFrom(): FromUpload {
            installException()
            method = "POST"
            return FromUpload(mConfig, threadPool)
        }

        /**
         *下载文件
         */
        fun downloadFile(): DownLoadFile{
            installException()
            method = "GET"
            return DownLoadFile(mConfig, threadPool)
        }


        /**
         * path加载图片
         */
        fun loadImageWithPath(target: View, path: String, net: Boolean = true, @DrawableRes errorId: Int = R.drawable.stroll_errorimg, listener: ImageListener? = null) {
            val convert = mConfig.imageConvert::class.java.newInstance()
            mConfig.imageConvert.loadUrl(mConfig,target,mConfig.baseUrl+path,net,errorId,listener)
        }

        /**
         * 全路径加载图片
         */
        fun loadImageWithUrl(target: View, url: String, net: Boolean = true, @DrawableRes errorId: Int = R.drawable.stroll_errorimg, listener: ImageListener? = null) {
            mConfig.imageConvert.loadUrl(mConfig,target,url,net,errorId,listener)
        }

        /**
         * 移除所有的请求
         */
        fun removeAll(){
            threadPool.removeAll()
        }


        /**
         * 网络框架未初始化异常
         */
        private fun installException(){
            if (!isInstall) throw RuntimeException("please write   Stroll.install(config)    in onCreate()")

            try {
                Class.forName("android.os.Handler") ?: throw RuntimeException("platfrom is not android ! please check platfrom")
            }catch (e: Exception){
                throw RuntimeException("platfrom is not android ! please check platfrom")
            }
        }

    }

    private var mBaseUrl = mConfig.baseUrl

    private var mPath: String = ""

    private val params: HashMap<String, String> = HashMap()

    private val headers: HashMap<String,String> = HashMap()

    private var mReadTimeOut: Int = mConfig.readTimeOut*1000

    private var mWriteTimeOut: Int = mConfig.writeTimeOut*1000

    private var mConnectTimeOut: Int = mConfig.connectTimeOut*1000

    private var tag: String = ""

    private var body: ByteArray = ByteArray(0)

    private var callBack: CallBack? = null



    /**
     * 设置基础url
     */
    fun setBaseUrl(baseUrl: String): Stroll{
        this.mBaseUrl = baseUrl
        return this
    }

    /**
     * 设置访问接口
     */
    fun setPath(path: String): Stroll{
        this.mPath = path
        return this
    }

    fun setParams(params: HashMap<String,String> = HashMap()): Stroll{
        this.params.putAll(params)
        return this
    }

    fun addParams(key: String, value: String): Stroll{
        this.params.put(key, value)
        return this
    }

    fun setHeaders(headers: HashMap<String,String> = HashMap()): Stroll{
        this.headers.putAll(headers)
        return this
    }

    fun addHeader(key: String,value: String): Stroll{
        this.headers.put(key, value)
        return this
    }

    fun setTag(tag: String): Stroll{
        this.tag = tag
        return this
    }

    fun setBody(body: String): Stroll{
        this.body = body.toByteArray(Charset.forName("UTF-8"))
        return this
    }

    /**
     * 添加网络读取数据超时时间 单位： s
     */
    fun setReadTimeOut(timeOut: Int): Stroll{
        this.mReadTimeOut = timeOut*1000
        return this
    }

    /**
     * 添加网络读取数据超时时间 单位： s
     */
    fun setWriteTimeOut(timeOut: Int): Stroll{
        this.mWriteTimeOut = timeOut*1000
        return this
    }

    /**
     * 添加网络连接超时时间 单位： s
     */
    fun setConnectTimeOut(timeOut: Int): Stroll{
        this.mConnectTimeOut = timeOut*1000
        return this
    }

    fun setCallBack(callBack: CallBack): Stroll{
        this.callBack = callBack
        return this
    }

    /**
     * 开始请求数据
     *
     */
    fun build(){
        val request = Request()
        request.setHttpConvert(mConfig.httpConvert)
        request.setUrl(this.mBaseUrl+this.mPath)
        request.setTag(this.tag)
        request.addParms(this.params)
        request.addHeaders(this.headers)
        request.setBody(this.body)
        request.setReadTimeOut(this.mReadTimeOut)
        request.setWriteTimeOut(this.mWriteTimeOut)
        request.setConnectTimeOut(this.mConnectTimeOut)
        request.setMethod(method)
        request.setSSLSocketFactory(mConfig.socketFactory)
        if (this.callBack == null) throw RuntimeException("请为本次请求添加回调函数")
        request.setCallBack(this.callBack!!)
        threadPool.execute(request)

    }


}