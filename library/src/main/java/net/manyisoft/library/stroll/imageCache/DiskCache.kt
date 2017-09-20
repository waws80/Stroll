package net.manyisoft.library.stroll.imageCache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import net.manyisoft.library.stroll.util.MD5Utils
import net.manyisoft.library.stroll.util.StrollLog
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileOutputStream


/**
 * 硬盘缓存类
 */

 class DiskCache: ImageCache {

    private var  PERCENT=90

    private var cacheDir: String


    constructor (cacheDir: String, percent: Int) {
        this.cacheDir = cacheDir
        if (percent in 1..99){
            PERCENT=percent
        }

    }

    override fun getCache(path: String): Bitmap? {
        val file= File("sdcard/"+cacheDir, MD5Utils.getMd5(path)+".jpg")
       StrollLog.msg("getCache: find image in $path diskcache")
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    override fun putCache(path: String, bitmap: Bitmap) {
        val f = File("sdcard/"+cacheDir)
        if (!f.exists()){
            f.mkdirs()
        }
        if (!f.exists())return
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos)
        bos.flush()
        bos.close()
        val file = File("sdcard/"+cacheDir,MD5Utils.getMd5(path)+".jpg")
        val out = FileOutputStream(file)
        out.write(bos.toByteArray(),0,bos.toByteArray().size)
        out.flush()
        out.close()
        //NativeUtil.compressBitmap(bitmap,PERCENT,file.absolutePath,true)
    }
}
