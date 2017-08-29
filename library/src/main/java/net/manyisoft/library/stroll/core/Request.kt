package net.manyisoft.library.stroll.core

import net.manyisoft.library.stroll.convert.HttpConvert
import net.manyisoft.library.stroll.socketfactory.DefaultSocketFactory
import net.manyisoft.library.stroll.util.StrollLog
import net.manyisoft.library.stroll.util.requestQueue
import net.manyisoft.library.stroll.util.uiHandler
import javax.net.ssl.SSLSocketFactory

/**
 * 网络请求类
 */
class Request : Runnable{

    private lateinit var mConvert: HttpConvert

    private lateinit var mUrl: String

    private lateinit var mParms: String

    private lateinit var mHeaders: HashMap<String,String>

    private var mReadTimeOut: Int = 6000

    private var mWriteTimeOut: Int = 6000

    private var mConnectTimeOut: Int = 6000

    private lateinit var method: String

    private var body: ByteArray = ByteArray(0)

    private lateinit var mCallBack: CallBack

    private var tag: String = ""

    private var mSSLSocketFactory: SSLSocketFactory = DefaultSocketFactory.getdefault()


    /**
     * 设置网络转换器
     */
    fun setHttpConvert(convert: HttpConvert): Request{
        this.mConvert = convert
        return this
    }

    /**
     * 设置url
     */
    fun setUrl(url: String): Request{
        this.mUrl = url
        return this
    }

    fun setTag(tag: String): Request{
        this.tag = tag
        return this
    }

    /**
     * 添加url查询条件
     */
    fun addParms(parms: HashMap<String, String>): Request{
        if (parms.isNotEmpty()){
            mParms="?"
            parms.forEach { mParms+= "${it.key}=${it.value}&" }
            mParms = mParms.substring(0,mParms.length-1)
        }else{
            mParms = ""
        }
        StrollLog.msg(mParms)
        return this
    }

    /**
     * 添加请求头
     */
    fun addHeaders(headers: HashMap<String,String> = HashMap()): Request{
        this.mHeaders = headers
        return this
    }

    /**
     * 添加请求体
     */
    fun setBody(body: ByteArray): Request{
        this.body = body
        return this
    }


    /**
     * 添加网络读取数据超时时间
     */
    fun setReadTimeOut(timeOut: Int): Request{
        this.mReadTimeOut = timeOut
        return this
    }

    /**
     * 添加网络读取数据超时时间
     */
    fun setWriteTimeOut(timeOut: Int): Request{
        this.mWriteTimeOut = timeOut
        return this
    }

    /**
     * 添加网络连接超时时间
     */
    fun setConnectTimeOut(timeOut: Int): Request{
        this.mConnectTimeOut = timeOut
        return this
    }

    /**
     * 添加网络请求方式
     */
    fun setMethod(method: String): Request{
        this.method = method
        return this
    }

    fun setSSLSocketFactory(sslSocketFactory: SSLSocketFactory): Request{
        this.mSSLSocketFactory = sslSocketFactory
        return this
    }

    /**
     * 添加网络请求回调接口
     */
    fun setCallBack(callBack: CallBack): Request{
        this.mCallBack = callBack
        return this
    }

    /**
     * 开始进行网络请求
     */
    override fun run() {
        //当请求方式为GET时url后面才可以添加查询条件，否则添加查询条件无效
        if (this.method == "GET"){
            this.mUrl +=mParms
            StrollLog.msg(this.mUrl)
        }
        requestQueue.put(this.mUrl,this.tag)
        mConvert::class.java.newInstance().call(this.mUrl,
                this.mHeaders,
                this.method,
                this.body,
                this.tag,
                this.mConnectTimeOut,
                this.mReadTimeOut,
                this.mWriteTimeOut,
                this.mSSLSocketFactory,
                this.mCallBack,
                uiHandler)
    }

}