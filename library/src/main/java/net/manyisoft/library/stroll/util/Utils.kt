package net.manyisoft.library.stroll.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * 工具类
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */

private val TAG = "Stroll"

object StrollLog{

    fun msg(msg: String, tag: String = TAG){
        log(msg, tag)
    }


    private fun log(msg: String, tag: String){
        Log.w(tag, msg)
    }

}

val requestQueue: ConcurrentHashMap<String,String> = ConcurrentHashMap()

val uiHandler: Handler by lazy { Handler(Looper.getMainLooper()) }


