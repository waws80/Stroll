package net.manyisoft.library.stroll.convert

import android.support.annotation.IdRes
import android.view.View
import net.manyisoft.library.stroll.core.StrollConfig
import net.manyisoft.library.stroll.img.LoadImage
import net.manyisoft.library.stroll.img.CacheType
import net.manyisoft.library.stroll.img.ImageListener

/**
 * 图片转换器默认实现类
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
class DefaultImageConvert : ImageConvert {
    override fun loadUrl(config: StrollConfig,
                         target: View,
                         url: String,
                         net: Boolean,
                         @IdRes errorId:Int,
                         listener: ImageListener?) {
        LoadImage(config)
                .setUrl(url)
                .setCache(CacheType.DOUBLECACHE)
                .setErrorImgId(errorId)
                .addTarget(target)
                .load(net,listener)
    }

}