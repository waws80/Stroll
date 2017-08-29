package net.manyisoft.library.stroll.convert

import android.os.Handler
import android.os.Message
import net.manyisoft.library.stroll.core.CallBack
import net.manyisoft.library.stroll.util.requestQueue
import java.util.concurrent.ConcurrentHashMap
import javax.net.ssl.SSLSocketFactory

/**
 * 网络转换器
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
interface HttpConvert {

    fun call(url: String,
             headers: HashMap<String,String> = HashMap(),
             method: String,
             body: ByteArray,
             tag: String,
             connectTimeOut: Int,
             readTimeOut: Int,
             writeTimeOut: Int,
             sSLSocketFactory: SSLSocketFactory,
             callBack: CallBack,
             handler: Handler)
}