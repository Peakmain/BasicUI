package com.peakmain.ui.utils.crash

/**
 * author ：Peakmain
 * createTime：2021/7/20
 * mail:2726449200@qq.com
 * describe：异常工具类
 */
object ExceptionUtils {
    @JvmStatic
    fun printException(e: Exception): String {
        val stackTrace = e.stackTrace ?: return ""
        val t = StringBuilder(e.toString())
        for (i in 2 until stackTrace.size) {
            t.append('[')
            t.append(stackTrace[i].className)
            t.append(':')
            t.append(stackTrace[i].methodName)
            t.append("(" + stackTrace[i].lineNumber + ")]")
            t.append("\n")
        }
        return t.toString()
    }
}