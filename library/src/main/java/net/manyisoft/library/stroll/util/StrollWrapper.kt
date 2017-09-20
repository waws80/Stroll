package net.manyisoft.library.stroll.util

import android.graphics.Bitmap
import android.view.View
import net.manyisoft.library.stroll.Stroll
import net.manyisoft.library.stroll.core.*
import net.manyisoft.library.stroll.img.ImageListener
import org.json.JSONObject
import java.io.File
import java.io.InputStream

/**
 * DSL
 */
open class DataWrapper {

    var baseUrl: String = Stroll.config?.baseUrl!!
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
    var _json:(JSONObject) -> Unit = {}

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

    fun resultJson(json: (JSONObject) -> Unit){
        this._json = json
    }
}

fun stroll_data(init: DataWrapper.() ->Unit){
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
            .setCallBack(object : JsonCallBack{
                override fun jsonComplate(json: JSONObject) {
                    wrap._json(json)
                }

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

fun stroll_post(init: PostDataWrapper.() ->Unit){
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
            .setCallBack(object : JsonCallBack{
                override fun jsonComplate(json: JSONObject) {
                    wrap._json(json)
                }

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

    var baseUrl: String = Stroll.config?.baseUrl!!
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

fun stroll_download(init: DownloadWrapper.() ->Unit){
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

class FormWrapper{
    var baseUrl: String = Stroll.config?.baseUrl!!
    var api: String = ""
    var formDatas = HashMap<String,String>()
    var headers = HashMap<String,String>()
    var tag: String = ""
    var filePaths = HashMap<String,String>()

    var _success:(String) ->Unit = {}
    var _complate:() ->Unit = {}
    var _progress:(Int) ->Unit = {}
    var _failer:(String) ->Unit = {}
    var _successJson:(JSONObject) -> Unit = {}

    fun result(success:(String) ->Unit){
        this._success = success
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
    fun resultJson(successJson:(JSONObject) -> Unit){
        this._successJson = successJson
    }
}

fun stroll_form(init: FormWrapper.() -> Unit){
    val wrap = FormWrapper()
    wrap.init()
    Stroll.uploadFrom()
            .setBaseUrl(wrap.baseUrl)
            .setPath(wrap.api)
            .addHeaders(wrap.headers)
            .setTag(wrap.tag)
            .setFromParams(wrap.formDatas)
            .setFilePaths(wrap.filePaths)
            .setCallBack(object : UploadJsonCallBack {
                override fun jsonComplate(json: JSONObject) {
                    wrap._successJson(json)
                }

                override fun complate() {
                    super.complate()
                    wrap._complate()
                }

                override fun progress(pro: Int) {
                    super.progress(pro)
                    wrap._progress(pro)
                }
                override fun success(text: String) {
                    wrap._success(text)
                }

                override fun error(msg: String) {
                    wrap._failer(msg)
                }

            }).build()


}

class MutableDataWrapper{

    var baseUrl: String = Stroll.config?.baseUrl!!
    var api: String = ""
    var headers = HashMap<String,String>()
    var tag: String = ""
    var data = ""
    var files = HashMap<String,File>()
    var fileStrs = HashMap<String,String>()
    var imgs = HashMap<String,Bitmap>()
    var imgStrs = HashMap<String,String>()

    var _success:(String) ->Unit = {}
    var _complate:() ->Unit = {}
    var _progress:(Int) ->Unit = {}
    var _failer:(String) ->Unit = {}
    var _successJson:(JSONObject) -> Unit = {}

    fun result(success:(String) ->Unit){
        this._success = success
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
    fun resultJson(successJson:(JSONObject) -> Unit){
        this._successJson = successJson
    }
}

fun stroll_mutableData(init: MutableDataWrapper.() -> Unit){
    val wrap = MutableDataWrapper()
    wrap.init()
    Stroll.uploadData().setBaseUrl(wrap.baseUrl)
            .setPath(wrap.api)
            .addHeaders(wrap.headers)
            .setTag(wrap.tag)
            .setData(wrap.data)
            .setFileStrs(wrap.fileStrs)
            .setFiles(wrap.files)
            .setImageStrs(wrap.imgStrs)
            .setImages(wrap.imgs)
            .setCallBack(object : UploadJsonCallBack {
                override fun jsonComplate(json: JSONObject) {
                    wrap._successJson(json)
                }

                override fun complate() {
                    super.complate()
                    wrap._complate()
                }

                override fun progress(pro: Int) {
                    super.progress(pro)
                    wrap._progress(pro)
                }
                override fun success(text: String) {
                    wrap._success(text)
                }

                override fun error(msg: String) {
                    wrap._failer(msg)
                }

            }).build()
}

class ImageWrapper{

    var target: View? = null

    var path: String = ""

    var errorRes: Int = -1

    var _progress:(Int) -> Unit ={}

    var _complate:() ->Unit ={}

    var _failer:() ->Unit = {}

    fun complate(complate:() ->Unit){
        this._complate = complate
    }
    fun progress(progress:(Int) ->Unit){
        this._progress = progress
    }
    fun failer(failer:() ->Unit){
        this._failer = failer
    }


}

fun stroll_img(init: ImageWrapper.() ->Unit){
    val wrap = ImageWrapper()
    wrap.init()
    Stroll.loadImage(wrap.target,wrap.path,wrap.errorRes,object : ImageListener{

        override fun progress(progress: Int) {
            wrap._progress(progress)
        }
        override fun complate() {
            wrap._complate()
        }
        override fun error() {
            wrap._failer()
        }
    })
}