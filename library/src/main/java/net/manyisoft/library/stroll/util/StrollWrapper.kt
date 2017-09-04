package net.manyisoft.library.stroll.util

import net.manyisoft.library.stroll.Stroll
import net.manyisoft.library.stroll.core.DownloadFileCallBack
import net.manyisoft.library.stroll.core.StringCallBack

/**
 * DSL
 */
open class DataWrapper {

    var baseUrl: String = ""
    var api: String = ""
    var params: HashMap<String,String> = HashMap()
    var headers: HashMap<String,String> = HashMap()
    var tag: String = ""
    var connectTimeOut: Int = 10
    var readTimeOut: Int = 10
    var _start:() ->Unit = {}
    var _complate:() ->Unit = {}
    var _result:(String) ->Unit = {}
    var _failer:(String) ->Unit = {}

    fun start(start:() ->Unit){
        this._start = start
    }
    fun complate(complate:() ->Unit){
        this._complate = complate
    }
    fun result(result:(String) ->Unit){
        this._result = result
    }
    fun failer(failer:(String) ->Unit){
        this._failer = failer
    }
}

fun data(init: DataWrapper.() ->Unit){
    val wrap = DataWrapper()
    wrap.init()
    Stroll.get()
            .setHeaders(wrap.headers)
            .setParams(wrap.params)
            .setBaseUrl(wrap.baseUrl)
            .setPath(wrap.api)
            .setConnectTimeOut(wrap.connectTimeOut)
            .setReadTimeOut(wrap.readTimeOut)
            .setTag(wrap.tag)
            .setCallBack(object : StringCallBack{
                override fun start() {
                    super.start()
                    wrap._start()
                }

                override fun complate() {
                    super.complate()
                    wrap._complate()
                }
                override fun success(text: String) {
                    wrap._result(text)

                }

                override fun error(msg: String) {
                    wrap._failer(msg)
                }

            })
            .build()

}

class PostDataWrapper : DataWrapper(){
    var body: String = ""
}

fun post(init: PostDataWrapper.() ->Unit){
    val wrap = PostDataWrapper()
    wrap.init()
    Stroll.post()
            .setHeaders(wrap.headers)
            .setParams(wrap.params)
            .setBaseUrl(wrap.baseUrl)
            .setPath(wrap.api)
            .setBody(wrap.body)
            .setConnectTimeOut(wrap.connectTimeOut)
            .setReadTimeOut(wrap.readTimeOut)
            .setTag(wrap.tag)
            .setCallBack(object : StringCallBack{
                override fun start() {
                    super.start()
                    wrap._start()
                }

                override fun complate() {
                    super.complate()
                    wrap._complate()
                }
                override fun success(text: String) {
                    wrap._result(text)

                }

                override fun error(msg: String) {
                    wrap._failer(msg)
                }

            })
            .build()
}

class DownloadWrapper{

    var baseUrl: String = ""
    var api: String = ""
    var params: HashMap<String,String> = HashMap()
    var headers: HashMap<String,String> = HashMap()
    var tag: String = ""
    var savePath: String = ""
    var fileName: String = ""
    var _start:() ->Unit = {}
    var _complate:() ->Unit = {}
    var _progress:(Int) ->Unit = {}
    var _failer:(String) ->Unit = {}

    fun start(start:() ->Unit){
        this._start = start
    }
    fun complate(complate:() ->Unit){
        this._complate = complate
    }
    fun progress(progress:(Int) ->Unit){
        this._progress = progress
    }
    fun failer(failer:(String) ->Unit){
        this._failer = failer
    }

}

fun download(init: DownloadWrapper.() ->Unit){
    val wrap = DownloadWrapper()
    wrap.init()
    Stroll.downloadFile()
            .setBaseUrl(wrap.baseUrl)
            .setPath(wrap.api)
            .addParams(wrap.params)
            .addHeaders(wrap.headers)
            .setTag(wrap.tag)
            .savePath(wrap.savePath , wrap.fileName)
            .setCallBack(object : DownloadFileCallBack{
                override fun start() {
                    wrap._start()
                }

                override fun progress(pro: Int) {
                    wrap._progress(pro)
                }

                override fun complate() {
                    wrap._complate()
                }

                override fun error(msg: String) {
                    wrap._failer(msg)
                }

            })
            .build()
}