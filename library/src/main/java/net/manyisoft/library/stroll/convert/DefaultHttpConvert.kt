package net.manyisoft.library.stroll.convert

import android.os.Handler
import net.manyisoft.library.stroll.core.CallBack
import net.manyisoft.library.stroll.util.StrollLog
import net.manyisoft.library.stroll.util.requestQueue
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

/**
 * 网络转换器默认实现类
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
class DefaultHttpConvert : HttpConvert {

    private val queue = requestQueue

    private lateinit var tag: String

    private lateinit var url: String

    override fun call(url: String,
                      headers: HashMap<String, String>,
                      method: String,
                      body: ByteArray,
                      tag: String,
                      connectTimeOut: Int,
                      readTimeOut: Int,
                      writeTimeOut: Int,
                      sSLSocketFactory: SSLSocketFactory,
                      callBack: CallBack,
                      handler: Handler) {

        this.tag = tag
        this.url = url
        headers.put("Connection","keep-alive")
        headers.put("Accept-Encoding", "identity")
        if (effective(tag, url)){
            //本次请求有效
            try {
                handler.post {
                    callBack.start()
                }

                request(url, headers, method, body, connectTimeOut, readTimeOut, sSLSocketFactory, callBack, handler)
            }catch (e: Exception){
                handler.post {
                    callBack.complate()
                    callBack.error("错误信息：${e.message}")
                    if (effective(tag, url) && tag.isNotEmpty()){
                        queue.remove(url)
                    }
                }
            }

        }else{
            //本次请求已被取消
            StrollLog.msg("无效的请求或本次请求已被取消")
        }

    }

    /**
     * 进行网络请求
     */
    private fun request(url: String, headers: HashMap<String, String>,
                        method: String, body: ByteArray,
                        connectTimeOut: Int, readTimeOut: Int,
                        sSLSocketFactory: SSLSocketFactory,
                        callBack: CallBack, handler: Handler){

        if (!url.startsWith("http")) throw RuntimeException("请设置正确的网络地址格式")
        val request: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        when {
            url.startsWith("https://") -> (request as HttpsURLConnection).sslSocketFactory = sSLSocketFactory
        }

        request.setChunkedStreamingMode(DEFAULT_BUFFER_SIZE)
        request.requestMethod = method
        request.connectTimeout = connectTimeOut
        request.readTimeout = readTimeOut
        headers.forEach { request.setRequestProperty(it.key,it.value) }
        if (method != "GET" && body.isNotEmpty()){
            //body中有数据要上传
            request.doOutput = true
            uploadData(request.outputStream,callBack,body,handler)
        }

        val code = request.responseCode
        if (code == HttpURLConnection.HTTP_OK){
            downLoadData(request,callBack,handler)
        }else{
            handler.post {
                callBack.complate()
                callBack.error("错误码：$code")
                if (effective(tag, url) && tag.isNotEmpty()){
                    queue.remove(url)
                }
            }
        }

    }

    /**
     * 向服务器获取数据
     */
    private fun downLoadData(request: HttpURLConnection, callBack: CallBack, handler: Handler) {
        callBack.asyncSuccess(request.contentLength.toLong(),request.inputStream)
        val str = String(request.inputStream.use { it.readBytes() }, Charset.forName("UTF-8"))
        handler.post {
            callBack.complate()
            callBack.success(str)
            if (effective(tag, url) && tag.isNotEmpty()){
                queue.remove(url)
            }
        }
    }

    /**
     * 向服务器上传数据
     */
    private fun uploadData(outputStream: OutputStream, callBack: CallBack, body: ByteArray,handler: Handler) {
        val contentLength = body.size
        val stream: InputStream = body.inputStream()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = stream.read(buffer)
        val dos = DataOutputStream(outputStream)
        var currentLength = 0
        while (bytes >0){
            dos.write(buffer,0,bytes)
            currentLength+=bytes
            val progress = ((currentLength.toFloat()/contentLength.toFloat())*100).toInt()
            handler.post {
                callBack.progress(progress)
                StrollLog.msg("总进度：$contentLength    当前进度： $currentLength")
            }
            bytes = stream.read(buffer)
        }
        dos.flush()
        dos.close()
    }


    /**
     * 请求是否有效
     * 无效则本次请求被取消了
     */
    private fun effective(tag: String,url: String)=
            tag.isEmpty() ||
                    (tag.isNotEmpty() &&
                            this.queue[url] != null &&
                            this.queue[url]?.isNotEmpty()!!)


}