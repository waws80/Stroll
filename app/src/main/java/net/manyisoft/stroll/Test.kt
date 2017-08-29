package net.manyisoft.stroll

import android.content.Context
import android.view.View
import android.widget.Toast
import net.manyisoft.library.stroll.Stroll
import net.manyisoft.library.stroll.core.DownloadFileCallBack
import net.manyisoft.library.stroll.core.JsonCallBack
import net.manyisoft.library.stroll.util.StrollLog
import org.json.JSONObject

/**
 * Created by liuxiongfei on 2017/8/28.
 */
object Test {


    fun  testHttp(context: Context,path: String,name: String){
        Stroll.install()

        //获取数据示例
//        Stroll.get()
//                .setBaseUrl("https://www.qigeairen.com")
//                .setCallBack(object : StringCallBack{
//                    override fun success(text: String) {
//                        StrollLog.msg("Test::"+Thread.currentThread().name+":\n"+text)
//                    }
//                    override fun error(msg: String) {
//                        StrollLog.msg("Test::"+Thread.currentThread().name+":\n"+msg)
//                    }
//                })
//                .build()

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

        (0 .. 1).forEach {
            Stroll.downloadFile()
                    .setBaseUrl("http://gdown.baidu.com/data/wisegame/a920cdeb1c1f59bc/baiduwangpan_527.apk")
                    .savePath(path, "$it$name")
                    .setCallBack(object : DownloadFileCallBack{
                        override fun start() {
                        }

                        override fun progress(pro: Int) {
                            StrollLog.msg("AA第$it 个的进度为：$pro")
                        }
                        override fun complate() {
                            StrollLog.msg("AA第$it 个下载完成！")
                            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show()
                        }

                        override fun error(msg: String) {
                            StrollLog.msg("AA第$it 个下载出错！$msg")
                            Toast.makeText(context,"下载出错$msg",Toast.LENGTH_SHORT).show()
                        }

                    })
                    .build()
        }

        //加载图片示例
//        val target = View(context)
//        val path = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1536086522,2785217828&fm=26&gp=0.jpg"
//        Stroll.loadImageWithUrl(target, path)
    }
}