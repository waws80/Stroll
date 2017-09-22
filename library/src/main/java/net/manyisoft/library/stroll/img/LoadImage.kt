package net.manyisoft.library.stroll.img

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.IdRes
import android.view.View
import android.widget.ImageView

import net.manyisoft.library.stroll.imageCache.MemeryCache
import net.manyisoft.library.stroll.imageCache.DoubleCache
import net.manyisoft.library.stroll.util.StrollLog
import net.manyisoft.library.stroll.Stroll
import net.manyisoft.library.stroll.core.StrollConfig

import net.manyisoft.library.stroll.R
import net.manyisoft.library.stroll.core.StringCallBack
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * 图片请求体
 */
class LoadImage(private val config: StrollConfig) {


    private var url: String = config.baseUrl


    private lateinit var target: View

    private var cache: CacheType = CacheType.MEMERYCACHE

    private var net: Boolean = true

    private var callBack: ImageListener? = null

    private  var errorImgId = R.drawable.stroll_errorimg


    private val uiHandler: Handler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val imageHolder= msg!!.obj as ImageHolder
                val path = imageHolder.url
                val bitmap = imageHolder.bitmap
                val view = imageHolder.target
                if (view!!.tag.toString() == path){
                    when(view){
                        is ImageView -> view.setImageBitmap(bitmap)
                        else -> view.setBackgroundDrawable(BitmapDrawable(bitmap))
                    }

                    if (callBack != null){
                        callBack!!.complate()
                    }

                }
            }
        }
    }


    fun setUrl(url: String): LoadImage {
        this.url = url
        return this
    }


    fun setCache(cacheType: CacheType): LoadImage {
        this.cache =cacheType
        return this
    }

    fun setErrorImgId(@IdRes id: Int): LoadImage {
        if (id != -1){
            this.errorImgId = id
        }
        return this
    }

    fun addTarget(target: View): LoadImage {
        this.target = target
        return this
    }


    /**
     * 获取图片
     */
    fun load(net: Boolean = true,callBack: ImageListener?){
        if (this.url.isNotEmpty()){
            this.target.tag = url
        }else{
            throw RuntimeException("请求图片的地址不能为空")
        }

        if (callBack != null){
            this.callBack = callBack
        }
        this.net = net
        val bitmap:Bitmap?
        when(cache){
            CacheType.MEMERYCACHE ->{
                bitmap = MemeryCache().getCache(url)
                check(bitmap,url,target,cache)
            }
            CacheType.DOUBLECACHE ->{
                bitmap = DoubleCache().getCache(url)
                check(bitmap,url,target,cache)
            }
        }

    }

    /**
     * 校验bitmap
     */
    private fun check(bitmap: Bitmap?, url: String, target: View,cacheType: CacheType){
        when (bitmap){
            null ->{
                if (net){
                    getNetImg( url, target ,cacheType)
                }else{
                    getNativeImg(url, target ,cacheType)
                }
            }
            else -> postBitmap(url,target,bitmap)
        }
    }

    /**
     * 获取网络图片
     */
    private fun getNetImg(url: String,target: View,cacheType: CacheType){
        if (!url.startsWith("http")) throw IllegalArgumentException("请求图片的url错误")
        Stroll.get().setBaseUrl(this.url).setCallBack(object : StringCallBack {
            override fun success(text: String) {}

            override fun progress(pro: Int) {
                super.progress(pro)
                uiHandler.post {
                    if (url == target.tag){
                        if (callBack != null){
                            callBack!!.progress(pro)
                        }
                    }

                }
            }

            override fun asyncSuccess(contentLength: Long, stream: InputStream) {
                super.asyncSuccess(contentLength, stream)
                val stream = loadProgress(contentLength, stream)
                val bitmap = BitmapUtils.getNetBitmap(stream.toByteArray(),target)
                if (bitmap != null){
                    when(cacheType){
                        CacheType.MEMERYCACHE ->{
                            val m = MemeryCache()
                            m.putCache(url,bitmap)
                            val b = m.getCache(url)
                            if (b == null){
                                postBitmap(url,target,bitmap)
                            }else{
                                postBitmap(url,target,b)
                            }

                        }
                        CacheType.DOUBLECACHE ->{
                            val d = DoubleCache()
                            d.putCache(url,bitmap)
                            val b = d.getCache(url)
                            if (b == null){
                                postBitmap(url,target,bitmap)
                            }else{
                                postBitmap(url,target,b)
                            }

                        }
                    }

                }else{
                    StrollLog.msg("请求图片错误。bitmap为空")
                    uiHandler.post {
                        if (url == target.tag){
                            if (callBack != null){
                                callBack!!.error()
                            }
                            target.setBackgroundResource(errorImgId)
                        }

                    }
                }
            }

            override fun error(msg: String) {
                StrollLog.msg("请求图片错误： $msg")
                uiHandler.post {
                    if (url == target.tag){
                        if (callBack != null){
                            callBack!!.error()
                        }
                        target.setBackgroundResource(errorImgId)
                    }

                }
            }

        }).build()

    }

    /**
     * 获取本地图片
     */
    private fun getNativeImg(url: String,target: View,cacheType: CacheType){
        val bitmap = BitmapUtils.getLocalBitmap(url,target)
        if (bitmap != null){
            when(cacheType){
                CacheType.MEMERYCACHE ->{
                    val m = MemeryCache()
                    m.putCache(url,bitmap)
                    val b = m.getCache(url)
                    postBitmap(url,target,b!!)
                }
                CacheType.DOUBLECACHE ->{
                    val d = DoubleCache()
                    d.putCache(url,bitmap)
                    val b = d.getCache(url)
                    postBitmap(url,target,b!!)
                }
            }

        }else{
            uiHandler.post {
                if (url == target.tag){
                    StrollLog.msg("请求图片错误。bitmap为空")
                    if (callBack != null){
                        callBack!!.error()
                    }
                    target.setBackgroundResource(errorImgId)
                }

            }
        }
    }



    private inner class ImageHolder{
        var url: String? = null
        var target: View? = null
        var bitmap: Bitmap? = null
    }

    private fun postBitmap( path: String, imageView: View, bitmap: Bitmap){
        val message = Message.obtain()
        val holder =  ImageHolder()
        holder.bitmap = bitmap
        holder.url = path
        holder.target = imageView
        message.obj = holder
        uiHandler.sendMessage(message)
    }


    private fun loadProgress(contentLength: Long , stream: InputStream): ByteArrayOutputStream{

        var currentLength = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = stream.read(buffer)
        val bos = ByteArrayOutputStream()
        while (bytes >0){
            bos.write(buffer,0,bytes)
            currentLength+=bytes
            val progress = ((currentLength.toFloat()/contentLength.toFloat())*100).toInt()
            uiHandler.post {
                StrollLog.msg("   总进度：$contentLength    当前进度： $currentLength")
                callBack?.progress(progress)
            }
            bytes = stream.read(buffer)
        }
        bos.flush()
        bos.close()
        return  bos
    }

}