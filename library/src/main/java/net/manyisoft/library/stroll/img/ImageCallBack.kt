package net.manyisoft.library.stroll.img

/**
 * Created by liuxiongfei
 */
interface ImageListener {

    fun progress(progress: Int)

    fun complate()

    fun error()
}