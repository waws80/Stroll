package net.manyisoft.library.stroll.core

import net.manyisoft.library.stroll.convert.DefaultHttpConvert
import net.manyisoft.library.stroll.convert.DefaultImageConvert
import net.manyisoft.library.stroll.convert.HttpConvert
import net.manyisoft.library.stroll.convert.ImageConvert
import net.manyisoft.library.stroll.socketfactory.DefaultSocketFactory
import javax.net.ssl.SSLSocketFactory

/**
 * 实体类
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */


data class StrollConfig(var baseUrl: String = "",//全局baseUrl
                        var globalHeaders: HashMap<String,String> = HashMap(),//全局头部
                        var httpConvert: HttpConvert = DefaultHttpConvert(), //全局网络转换器
                        var imageConvert: ImageConvert = DefaultImageConvert(),//全局图片转换器
                        //全局支持https请求
                        var socketFactory: SSLSocketFactory = DefaultSocketFactory.getdefault(),
                        //全局读取数据超时时间 和全局写数据超时时间（目前用不到）
                        var readTimeOut: Int = 6,var writeTimeOut: Int = 6,
                        //全局连接服务器超时时间
                        var connectTimeOut: Int = 6)
