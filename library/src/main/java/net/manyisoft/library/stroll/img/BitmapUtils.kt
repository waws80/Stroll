package net.manyisoft.library.stroll.img

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View

/**
 * 图片处理类
 */

object BitmapUtils {


    private fun getBitmap(path: String , imageView: View):Bitmap {


        val options = BitmapFactory.Options()
        options.inJustDecodeBounds=true
        BitmapFactory.decodeFile(path,options)
        val imageSize = ImageSizeUtil.getImageViewSize(imageView)
        options.inSampleSize= ImageSizeUtil.caculateInSampleSize(options, imageSize.width!!, imageSize.height!!)
        options.inJustDecodeBounds=false
        return BitmapFactory.decodeFile(path,options)
    }

    private fun  getBitmapFromByte( path: ByteArray, imageView: View): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds=true
        BitmapFactory.decodeByteArray(path,0,path.size)
        val imageSize = ImageSizeUtil.getImageViewSize(imageView)
        options.inSampleSize= ImageSizeUtil.caculateInSampleSize(options, imageSize.width!!, imageSize.height!!)
        options.inJustDecodeBounds=false
        return  BitmapFactory.decodeByteArray(path,0,path.size)
    }


     fun getNetBitmap(byteArray: ByteArray,  imageView: View): Bitmap? =  getBitmapFromByte(byteArray,imageView)


     fun getLocalBitmap( path: String,  imageView: View): Bitmap? = getBitmap(path,imageView)


}
