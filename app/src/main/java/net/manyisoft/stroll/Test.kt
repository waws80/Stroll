package net.manyisoft.stroll

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import net.manyisoft.library.stroll.Stroll
import net.manyisoft.library.stroll.core.CallBack
import net.manyisoft.library.stroll.core.UploadCallBack
import net.manyisoft.library.stroll.img.ImageListener
import net.manyisoft.library.stroll.util.*
import org.json.JSONObject
import java.io.File
import java.io.InputStream

/**
 * Created by liuxiongfei on 2017/8/28.
 */
object Test {


    fun  testHttp(iv: TextView, iv1: TextView, iv2: TextView, iv3: TextView, iv4: TextView, context: Context, mpath: String, name: String){
        Stroll.install()

        //获取数据示例
//        Stroll.get()
//                .setBaseUrl("https://www.qigeairen.com")
//                .setCallBack(object : StringCallBack {
//                    override fun success(text: String) {
//                        StrollLog.msg(text)
//                    }
//                    override fun error(msg: String) {
//                        StrollLog.msg(msg)
//                    }
//                })
//                .build()
//        data {
//            baseUrl = "https://www.qigeairen.com"
//            result { text -> StrollLog.msg(text)
//                Toast.makeText(context,"获取的数据： $text",Toast.LENGTH_SHORT).show()}
//            failer { msg -> StrollLog.msg(msg)
//                Toast.makeText(context,"获取数据出错： $msg",Toast.LENGTH_SHORT).show()}
//        }

        //获取json数据示例

//        Stroll.get().setBaseUrl("https://raw.githubusercontent.com/")
//                .setPath("waws80/waws80.github.io/master/test.json")
//                .setCallBack(object : JsonCallBack{
//                    override fun error(msg: String) {
//                        StrollLog.msg(msg)
//                        Toast.makeText(context,"获取数据出错： $msg",Toast.LENGTH_SHORT).show()
//                    }
//
//                    override fun jsonComplate(json: JSONObject) {
//                        StrollLog.msg("$json")
//                        Toast.makeText(context,"$json",Toast.LENGTH_SHORT).show()
//                    }
//
//                })
//                .build()


        //下载文件示例(不支持批量下载大文件)

//        (1 .. 5).forEach {
//            Stroll.downloadFile()
//                    .setBaseUrl("http://gdown.baidu.com/data/wisegame/a920cdeb1c1f59bc/baiduwangpan_527.apk")
//                    .savePath(path, "$it$name")
//                    .setCallBack(object : DownloadFileCallBack{
//                        override fun start() {
//                        }
//
//                        override fun progress(pro: Int) {
//                            StrollLog.msg("下载文件：---第$it 个的进度为：$pro")
//                            when(it){
//                                1 -> iv.text = "下载进度为：$pro"
//                                2 -> iv1.text = "下载进度为：$pro"
//                                3 -> iv2.text = "下载进度为：$pro"
//                                4 -> iv3.text = "下载进度为：$pro"
//                                5 -> iv4.text = "下载进度为：$pro"
//
//                            }
//                        }
//                        override fun complate() {
//                            StrollLog.msg("下载文件：---第$it 个下载完成！")
//                            when(it){
//                                1 -> iv.text = "下载完成"
//                                2 -> iv1.text = "下载完成"
//                                3 -> iv2.text = "下载完成"
//                                4 -> iv3.text = "下载完成"
//                                5 -> iv4.text = "下载完成"
//
//                            }
//                            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun error(msg: String) {
//                            when(it){
//                                1 -> iv.text = "下载出错"
//                                2 -> iv1.text = "下载出错"
//                                3 -> iv2.text = "下载出错"
//                                4 -> iv3.text = "下载出错"
//                                5 -> iv4.text = "下载出错"
//
//                            }
//                            StrollLog.msg("下载文件：---第$it 个下载出错！$msg")
//                            Toast.makeText(context,"下载出错$msg",Toast.LENGTH_SHORT).show()
//                        }
//
//                    })
//                    .build()
//        }

        //加载图片示例
//        val target = View(context)
//        val url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1536086522,2785217828&fm=26&gp=0.jpg"
////        Stroll.loadImageWithUrl(target, path)
//
//        Stroll.loadImageWithUrl(target, url,true, listener = object : ImageListener{
//            override fun progress(progress: Int) {
//
//            }
//
//            override fun complate() {
//            }
//
//            override fun error() {
//            }
//
//        })

        //文件上传示例
//        val files = HashMap<String,String>()
//        files.put("file","/sdcard/standerPhoto/1505291618.jpeg")
//        Stroll.uploadFrom()
//                .setBaseUrl("http://192.168.0.101:8080/thanatos/upload")
//                //.setBaseUrl("http://101.200.60.239:80/")
//               // .setPath("img/upload/img/jhtml")
//                .setFilePaths(files).setCallBack(object : UploadCallBack{
//
//            override fun error(msg: String) {
//                log(msg)
//            }
//
//            override fun progress(pro: Int) {
//                log("当前进度：$pro")
//            }
//
//            override fun success(text: String) {
//                log(text)
//            }
//
//        }).build()

        //上传dsl
//        val mfiles = HashMap<String,String>()
//        mfiles.put("file","/sdcard/standerPhoto/1505291618.jpeg")
//        val mfilesd = HashMap<String,String>()
//        mfilesd.put("file","zhangsan张三")
//        form {
//            baseUrl = "http://192.168.0.100:8080/thanatos/upload"
//            filePaths = mfiles
//            formDatas = mfilesd
//            success { s -> log(s) }
//            progress { p -> log("当前进度：$p") }
//            failer { s -> log(s) }
//        }
        val mfile = HashMap<String,File>()
        mfile.put("file", File("/sdcard/standerPhoto/1505291618.jpeg"))
        val mData = JSONObject().put("from","我来自mutableData").toString()
        val mImg = HashMap<String, Bitmap>()
        mImg.put("img",BitmapFactory.decodeFile("/sdcard/standerPhoto/1505291618.jpeg"))
        stroll_mutableData {
            baseUrl = "http://192.168.0.100:8080/thanatos/upload"
            files = mfile
            data = mData
            imgs = mImg
            result { s -> log(s) }
            progress { p -> log("当前进度：$p")}
            failer { s -> log(s) }
        }

        stroll_img {
            target = iv1
            path = "/sdcard/standerPhoto/1505291618.jpeg"
            complate {
                log("家在本地图片完成")
            }
            failer {
                log("家在本地图片出错")
            }
        }




        //文件下载示例
//        (1 .. 5).forEach {
//            download {
//                baseUrl = "http://gdown.baidu.com/data/wisegame/a920cdeb1c1f59bc/baiduwangpan_527.apk"
//                savePath = path
//                fileName = "$it$name"
//                progress { pro ->
//                    when (it) {
//                        1 -> iv.text = "下载进度为：$pro"
//                        2 -> iv1.text = "下载进度为：$pro"
//                        3 -> iv2.text = "下载进度为：$pro"
//                        4 -> iv3.text = "下载进度为：$pro"
//                        5 -> iv4.text = "下载进度为：$pro"
//
//                    }
//                }
//                complate {
//                    when(it){
//                        1 -> iv.text = "下载完成"
//                        2 -> iv1.text = "下载完成"
//                        3 -> iv2.text = "下载完成"
//                        4 -> iv3.text = "下载完成"
//                        5 -> iv4.text = "下载完成"
//
//                    }
//                }
//                failer { msg ->
//                    when(it){
//                        1 -> iv.text = "1下载出错:$msg"
//                        2 -> iv1.text = "2下载出错:$msg"
//                        3 -> iv2.text = "3下载出错:$msg"
//                        4 -> iv3.text = "4下载出错:$msg"
//                        5 -> iv4.text = "5下载出错:$msg"
//
//                    }
//                }
//            }
//        }
    }

    fun log(msg: String){
        Log.d("上传示例：",msg)
    }
}