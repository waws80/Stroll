package net.manyisoft.library.stroll.core

import net.manyisoft.library.stroll.threadpool.ThreadPool
import net.manyisoft.library.stroll.util.StrollLog
import net.manyisoft.library.stroll.util.uiHandler
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * @author liuxiongfei
 * 数据下载核心实现类.
 */
class DownLoadFile (private val config: StrollConfig, private val threadPool: ThreadPool) {

    private var params: HashMap<String,String> = HashMap()

    private var callBack: DownloadFileCallBack? = null

    private var filePath: String = ""

    private var fileName: String = ""


    private var mBaseUrl = config.baseUrl

    private var mPath: String = ""

    private val headers: HashMap<String,String> = config.globalHeaders

    private var mReadTimeOut: Int = config.readTimeOut*1000

    private var mWriteTimeOut: Int = config.writeTimeOut*1000

    private var mConnectTimeOut: Int = config.connectTimeOut*1000

    private var tag: String = ""

    private lateinit var file: File



    fun setBaseUrl(baseUrl: String): DownLoadFile{
        this.mBaseUrl = baseUrl
        return this
    }

    fun setPath(path: String): DownLoadFile{
        this.mPath = path
        return this
    }

    fun setHeader(key: String, value: String): DownLoadFile{
        this.headers.put(key, value)
        return this
    }

    fun addHeaders(headers: HashMap<String,String> ): DownLoadFile{
        this.headers.putAll(headers)
        return this
    }

    fun setReadTimeOut(timeOut: Int): DownLoadFile {
        this.mReadTimeOut = timeOut
        return this
    }

    fun setWriteTimeOut(timeOut: Int): DownLoadFile {
        this.mWriteTimeOut = timeOut
        return this
    }

    fun setConnectTimeOut(timeOut: Int): DownLoadFile {
        this.mConnectTimeOut = timeOut
        return this
    }

    fun setTag(tag: String): DownLoadFile {
        this.tag = tag
        return this
    }


    fun setParams(key: String,value: String): DownLoadFile{
        this.params.put(key, value)
        return this
    }

    fun addParams(params: HashMap<String,String> = HashMap()): DownLoadFile{
        this.params.putAll(params)
        return this
    }

    fun setCallBack(callBack: DownloadFileCallBack): DownLoadFile {
        this.callBack = callBack
        return this
    }

    fun savePath(path: String, name: String): DownLoadFile{
        this.filePath = path
        this.fileName = name
        return this
    }

    fun build(){
        if (this.callBack == null) throw RuntimeException("请为本次请求添加回调函数")
        file = File(this.filePath+"/"+this.fileName)
        if (file.exists()){
            uiHandler.post { callBack?.error("当前文件已存在！") }
            return
        }
        val request = Request()
        request.setHttpConvert(config.httpConvert)
        request.setUrl(this.mBaseUrl+this.mPath)
        request.addParms(this.params)
        request.setTag(this.tag)
        request.addHeaders(this.headers)
        request.setReadTimeOut(this.mReadTimeOut)
        request.setWriteTimeOut(this.mWriteTimeOut)
        request.setConnectTimeOut(this.mConnectTimeOut)
        request.setMethod("GET")
        request.setSSLSocketFactory(config.socketFactory)
        val call: DownloadFileCallBack = this.callBack!!
        request.setCallBack(object : StringCallBack{
            override fun start() {
                super.start()
                call.start()
            }

            override fun progress(pro: Int) {
                super.progress(pro)
                call.progress(pro)
            }

            override fun success(text: String) {}

            override fun asyncSuccess(contentLength: Long, stream: InputStream) {
                super.asyncSuccess(contentLength, stream)
                parseResult(call,contentLength,stream)
            }

            override fun error(msg: String) {
                call.error(msg)
            }

        })

        threadPool.execute(request)

    }

    private fun parseResult(callBack: DownloadFileCallBack, contentLength: Long , stream: InputStream) {

        val file = File(this.filePath+"/"+this.fileName)
        if (file.exists()){
            uiHandler.post { callBack.error("当前文件已存在！") }
        }else{

        var currentLength = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = stream.read(buffer)
        val out = FileOutputStream(file)
        while (bytes >0){
            out.write(buffer,0,bytes)
            currentLength+=bytes
            val progress = ((currentLength.toFloat()/contentLength.toFloat())*100).toInt()
            uiHandler.post {
                StrollLog.msg("   总进度：$contentLength    当前进度： $currentLength")
                callBack.progress(progress)
            }
            bytes = stream.read(buffer)
        }
            out.flush()
            out.close()

            uiHandler.post { callBack.complate() }
        }



    }


}