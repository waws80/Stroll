package net.manyisoft.library.stroll.core

import net.manyisoft.library.stroll.threadpool.ThreadPool
import net.manyisoft.library.stroll.util.StrollLog
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset


/**
 * @author liuxiongfei
 * 文件上传核心实现类.
 */
class FromUpload(val config: StrollConfig, val threadPool: ThreadPool) : Upload{

    private var body: ByteArray = ByteArray(0)

    private var files: MutableList<File> = mutableListOf()

    private var params: HashMap<String,String> = HashMap()

    private var callBack: CallBack? = null



    private var mBaseUrl = config.baseUrl

    private var mPath: String = ""

    private val headers: HashMap<String,String> = config.globalHeaders

    private var mReadTimeOut: Int = config.readTimeOut*1000

    private var mWriteTimeOut: Int = config.writeTimeOut*1000

    private var mConnectTimeOut: Int = config.connectTimeOut*1000

    private var tag: String = ""



    override fun setBaseUrl(baseUrl: String): FromUpload{
        this.mBaseUrl = baseUrl
        return this
    }

    override fun setPath(path: String): FromUpload{
        this.mPath = path
        return this
    }

    override fun setHeader(key: String, value: String): FromUpload{
        this.headers.put(key, value)
        return this
    }

    override fun addHeaders(headers: HashMap<String,String> ): FromUpload{
        this.headers.putAll(headers)
        return this
    }

    override fun setReadTimeOut(timeOut: Int): FromUpload {
        this.mReadTimeOut = timeOut
        return this
    }

    override fun setWriteTimeOut(timeOut: Int): FromUpload {
        this.mWriteTimeOut = timeOut
        return this
    }

    override fun setConnectTimeOut(timeOut: Int): FromUpload {
        this.mConnectTimeOut = timeOut
        return this
    }

    override fun setTag(tag: String): FromUpload {
        this.tag = tag
        return this
    }



    fun setCallBack(callBack: CallBack): FromUpload {
        this.callBack = callBack
        return this
    }

    fun setFilePaths(filePaths: MutableList<String>): FromUpload{
        try {
            filePaths.forEach {
                val file = File(it)
                if (file.exists()){
                    this.files.add(file)
                }else{
                    StrollLog.msg("当前文件不存在：$it")
                }
            }

        }catch (e: Exception){
            StrollLog.msg(e.message!!)
        }
        return this

    }

    /**
     * 添加表单数据
     */
    fun setFromParams(params: HashMap<String,String>): FromUpload{
        this.params = params
        return this
    }

    override fun build() {
        Thread{
            makeBody()
            val request = Request()
            request.setHttpConvert(config.httpConvert)
            request.setUrl(this.mBaseUrl+this.mPath)
            request.setTag(this.tag)
            request.addHeaders(this.headers)
            request.setBody(this.body)
            request.setReadTimeOut(this.mReadTimeOut)
            request.setWriteTimeOut(this.mWriteTimeOut)
            request.setConnectTimeOut(this.mConnectTimeOut)
            request.setMethod("POST")
            request.setSSLSocketFactory(config.socketFactory)
            if (this.callBack == null) throw RuntimeException("请为本次请求添加回调函数")
            request.setCallBack(this.callBack!!)
            threadPool.execute(request)
        }.run()
    }


    /**
     * 构建上传服务器的请求体
     */
    fun makeBody(){
        val end = "\r\n"
        val twoHyphens = "--"
        val boundary = "******"
        headers.put("Charset", "UTF-8")
        headers.put("Content-Type","multipart/form-data;boundary=" + boundary)

        //添加表单数据

        val from = StringBuilder()
        if (this.params.isNotEmpty()){
            this.params.forEach {
                from.append(twoHyphens+boundary+end)
                from.append("Content-Disposition: form-data; name=\"${it.key}\"$end")
                from.append("Content-Type: text/plain; charset=UTF-8$end")
                from.append("Content-Transfer-Encoding: 8bit$end")
                from.append(end)
                from.append(it.value)
                from.append(end)
            }
        }

        val b1 = from.toString().toByteArray(Charset.forName("UTF-8"))

        //添加多文件
        var b2 = ByteArray(0)
        if (this.files.isNotEmpty()){
            (0 .. this.files.size).forEach {
                val path: String = this.files[it].absolutePath
                val fileSb = StringBuilder()
                fileSb.append(twoHyphens+boundary+end)
                fileSb.append("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""+
                        path.substring(path.lastIndexOf("/")+1)+
                        "\""+
                        end)
                fileSb.append(end)
                val bFileSB = fileSb.toString().toByteArray(Charset.forName("UTF-8"))

                val bFile = FileInputStream(this.files[it]).use { it.readBytes() }

                val bEnd = end.toByteArray(Charset.forName("UTF-8"))
                b2+= bFileSB+bFile+bEnd
            }
        }

        //添加请求结束标志
        val b3 = (twoHyphens+boundary+twoHyphens+end).toByteArray(Charset.forName("UTF-8"))

        StrollLog.msg("b1:${b1.size}\nb2:${b2.size}\nb3:${b3.size}\n" +
                "total:${b1.size+b2.size+b3.size}")
        StrollLog.msg("添加文件前body：${body.size}")
        body = b1+b2+b3
        StrollLog.msg("添加文件后body：${body.size}")
    }
}

