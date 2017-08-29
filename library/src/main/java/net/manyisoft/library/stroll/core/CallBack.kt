package net.manyisoft.library.stroll.core

import org.json.JSONObject

/**
 * 网络访问数据成功回调函数
 */


interface CallBack{

    fun start()

    fun progress(pro: Int)

    fun complate()

    fun asyncSuccess( byteArray: ByteArray)

    fun success(text: String)

    fun error(msg: String)
}

interface ImageCallBack : CallBack{

    override fun start(){}

    override fun progress(pro: Int){}

    override fun complate(){}

    override fun success(text: String){}

}

interface StringCallBack : CallBack{

    override fun start() {
    }

    override fun progress(pro: Int) {
    }

    override fun complate() {
    }

    override fun asyncSuccess(byteArray: ByteArray) {
    }

}

interface JsonCallBack : CallBack{

    override fun start(){}

    override fun progress(pro: Int){}

    override fun complate(){}

    override fun asyncSuccess( byteArray: ByteArray){}

    override fun success(text: String){
        jsonComplate(JSONObject(text))
    }

    fun jsonComplate(json: JSONObject)

}


interface DownloadFileCallBack {

    fun start()

    fun progress(pro: Int)

    fun complate()

    fun error(msg: String)


}