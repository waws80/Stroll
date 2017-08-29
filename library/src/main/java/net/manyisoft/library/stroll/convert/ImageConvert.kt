package net.manyisoft.library.stroll.convert

import android.support.annotation.IdRes
import android.view.View
import net.manyisoft.library.stroll.core.StrollConfig
import net.manyisoft.library.stroll.img.ImageListener

/**
 * 图片加载转换器
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
interface ImageConvert {

    fun loadUrl(config: StrollConfig,
                target: View,
                url: String,
                net: Boolean,
                @IdRes errorId:Int,
                listener: ImageListener?)

}