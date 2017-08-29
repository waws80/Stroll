package net.manyisoft.library.stroll.core

import android.graphics.Bitmap
import android.util.Base64
import net.manyisoft.library.stroll.threadpool.ThreadPool
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

/**
 * @author liuxiongfei
 * 数据上传核心实现类.
 * 与from表单提交不同的是提交到后台的数据为json格式数据格式如下：
 * { "data":"文本数据", "images":["","",""],"files":[{"fileName":"a.text","str":""},{"fileName":"a.jpeg","str":""}]}
 * 说明： data: 可以是字符串 json数组（内部自由组合）
 *       image: Base64字符串 以json数组形式保存 （内部自由组合）
 *       file: Base64字符串 以json数组形式保存 （内部自由组合）
 */
class DataUpload (private val config: StrollConfig, private val threadPool: ThreadPool) : Upload{


    private var body: ByteArray = ByteArray(0)

    private var callBack: CallBack? = null

    private var data: String = ""

    private var imageStrs: MutableList<String> = mutableListOf()

    private var fileStrs: MutableList<String> = mutableListOf()

    private var images: MutableList<Bitmap> = mutableListOf()

    private var files: MutableList<File> = mutableListOf()


    private var mBaseUrl = config.baseUrl

    private var mPath: String = ""

    private val headers: HashMap<String,String> = config.globalHeaders

    private var mReadTimeOut: Int = config.readTimeOut*1000

    private var mWriteTimeOut: Int = config.writeTimeOut*1000

    private var mConnectTimeOut: Int = config.connectTimeOut*1000

    private var tag: String = ""



    override fun setBaseUrl(baseUrl: String): DataUpload{
        this.mBaseUrl = baseUrl
        return this
    }

    override fun setPath(path: String): DataUpload{
        this.mPath = path
        return this
    }

    override fun setHeader(key: String, value: String): DataUpload{
        this.headers.put(key, value)
        return this
    }

    override fun addHeaders(headers: HashMap<String,String> ): DataUpload{
        this.headers.putAll(headers)
        return this
    }

    override fun setReadTimeOut(timeOut: Int): DataUpload {
        this.mReadTimeOut = timeOut
        return this
    }

    override fun setWriteTimeOut(timeOut: Int): DataUpload {
        this.mWriteTimeOut = timeOut
        return this
    }

    override fun setConnectTimeOut(timeOut: Int): DataUpload {
        this.mConnectTimeOut = timeOut
        return this
    }

    override fun setTag(tag: String): DataUpload {
        this.tag = tag
        return this
    }


    fun setData(data: String): DataUpload{
        this.data = data
        return this
    }

    fun setImageStrs(base64strs: MutableList<String>): DataUpload{
        this.imageStrs = base64strs
        return this
    }

    fun setFileStrs(strs: MutableList<String>): DataUpload{
        this.fileStrs = strs
        return this
    }

    fun setImages(bitmaps: MutableList<Bitmap>): DataUpload{
        this.images = bitmaps
        return this
    }

    fun setFiles(files: MutableList<File>): DataUpload{
        this.files = files
        return this
    }


    fun setCallBack(callBack: CallBack): DataUpload {
        this.callBack = callBack
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
     * 制作body体
     * { "data":"文本数据", "images":["","",""],"files":[{"fileName":"a.text","str":""},{"fileName":"a.jpeg","str":""}]}
     */
    private fun makeBody(){

        val json = JSONObject()

        //添加数据
        json.put("data",this.data)

        //添加图片
        val images = JSONArray()

        if (this.imageStrs.isNotEmpty()){
            this.imageStrs.forEach { images.put(it) }
        }
        if (this.images.isNotEmpty()){
            this.images.forEach {
                val bos = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG,100,bos)
                bos.flush()
                bos.close()
                images.put(Base64.encodeToString(bos.toByteArray(),Base64.DEFAULT))
            }
        }
        json.put("images",images)

        //添加文件
        val files = JSONArray()

        if (this.fileStrs.isNotEmpty()){
            this.fileStrs.forEach { files.put(it) }
        }

        //{"fileName":"a.text","str":""}
        if (this.files.isNotEmpty()){
            this.files.forEach {
                val path = it.absolutePath
                val name = path.substring(path.lastIndexOf("/")+1)
                val str = Base64.encodeToString(FileInputStream(it).use { it.readBytes() },Base64.DEFAULT)
                val fileJson = JSONObject()
                fileJson.put("fileName",name)
                fileJson.put("str",str)
                files.put(fileJson)
            }
        }


        //将组合的数据组合成body
        this.body = json.toString().toByteArray(Charset.forName("UTF-8"))

    }
}