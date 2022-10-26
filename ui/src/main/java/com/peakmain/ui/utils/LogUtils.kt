package com.peakmain.ui.utils

import android.util.Log
import androidx.annotation.IntDef
import com.peakmain.ui.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * author ：Peakmain
 * createTime：2021/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class LogUtils private constructor() {
    @IntDef(V, D, I, W, E, A)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class TYPE
    private class TagHead internal constructor(var tag: String?, var consoleHead: Array<String?>?, var fileHead: String)

    companion object {
        const val V = Log.VERBOSE
        const val D = Log.DEBUG
        const val I = Log.INFO
        const val W = Log.WARN
        const val E = Log.ERROR
        const val A = Log.ASSERT
        var isDebug = true

        private val T = charArrayOf('V', 'D', 'I', 'W', 'E', 'A')
        private const val FILE = 0x10
        private const val JSON = 0x20
        private const val XML = 0x30
        private var sExecutor: ExecutorService? = null
        private val sDefaultDir // log默认存储目录
                : String? = null
        private val sDir // log存储目录
                : String? = null
        private const val sFilePrefix = "util" // log文件前缀
        private const val sLogSwitch = true // log总开关，默认开
        private const val sLog2ConsoleSwitch = true // logcat是否打印，默认打印
        private val sGlobalTag: String? = null // log标签
        private const val sTagIsSpace = true // log标签是否为空白
        private const val sLogHeadSwitch = true // log头部开关，默认开
        private const val sLog2FileSwitch = false // log写入文件开关，默认关
        private const val sLogBorderSwitch = true // log边框开关，默认开
        private const val sConsoleFilter = V // log控制台过滤器
        private const val sFileFilter = V // log文件过滤器
        private const val sStackDeep = 1 // log栈深度
        private val FILE_SEP = System.getProperty("file.separator")
        private val LINE_SEP = System.getProperty("line.separator")
        private const val TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
        private const val SPLIT_BORDER = "╟───────────────────────────────────────────────────────────────────────────────────────────────────"
        private const val LEFT_BORDER = "║ "
        private const val BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"
        private const val MAX_LEN = 4000
        private val FORMAT: Format = SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault())
        private const val NULL_TIPS = "Log with null object."
        private const val NULL = "null"
        private const val ARGS = "args"
        @JvmStatic
        fun v(contents: Any?) {
            log(V, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun v(tag: String?, content: Any?, vararg contents: Any?) {
            log(V, tag, content!!, contents)
        }

        @JvmStatic
        fun d(contents: Any?) {
            log(D, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun d(tag: String?, content: Any?, vararg contents: Any?) {
            log(D, tag, content!!, contents)
        }
        @JvmStatic
        fun i(contents: Any?) {
            log(I, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun normal(message: String) {
            Log.i(BuildConfig.TAG, message)
        }
        @JvmStatic
        fun i(tag: String?, content: Any?, vararg contents: Any?) {
            log(I, tag, content!!, contents)
        }
        @JvmStatic
        fun w(contents: Any?) {
            log(W, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun w(tag: String?, content: Any?, vararg contents: Any?) {
            log(W, tag, content!!, contents)
        }
        @JvmStatic
        fun e(contents: Any?) {
            log(E, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun e(tag: String?, content: Any?, vararg contents: Any?) {
            log(E, tag, content!!, contents)
        }
        @JvmStatic
        fun a(contents: Any?) {
            log(A, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun a(tag: String?, content: Any?, vararg contents: Any?) {
            log(A, tag, content!!, contents)
        }
        @JvmStatic
        fun file(contents: Any?) {
            log(FILE or D, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun file(@TYPE type: Int, contents: Any?) {
            log(FILE or type, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun file(tag: String?, contents: Any?) {
            log(FILE or D, tag, contents!!)
        }
        @JvmStatic
        fun file(@TYPE type: Int, tag: String?, contents: Any?) {
            log(FILE or type, tag, contents!!)
        }
        @JvmStatic
        fun json(contents: String?) {
            log(JSON or D, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun json(@TYPE type: Int, contents: String?) {
            log(JSON or type, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun json(tag: String?, contents: String?) {
            log(JSON or D, tag, contents!!)
        }
        @JvmStatic
        fun json(@TYPE type: Int, tag: String?, contents: String?) {
            log(JSON or type, tag, contents!!)
        }
        @JvmStatic
        fun xml(contents: String?) {
            log(XML or D, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun xml(@TYPE type: Int, contents: String?) {
            log(XML or type, sGlobalTag, contents!!)
        }
        @JvmStatic
        fun xml(tag: String?, contents: String?) {
            log(XML or D, tag, contents!!)
        }
        @JvmStatic
        fun xml(@TYPE type: Int, tag: String?, contents: String?) {
            log(XML or type, tag, contents!!)
        }

        private fun log(type: Int, tag: String?, vararg contents: Any) {
            if (BuildConfig.DEBUG) {
                val typeLow = type and 0x0f
                val typeHigh = type and 0xf0
                if (typeLow < sConsoleFilter && typeLow < sFileFilter) return
                val tagHead = processTagAndHead(tag)
                val body = processBody(typeHigh, *contents)
                if (sLog2ConsoleSwitch && typeLow >= sConsoleFilter && typeHigh != FILE) {
                    print2Console(typeLow, tagHead.tag, tagHead.consoleHead, body)
                }
                if ((sLog2FileSwitch || typeHigh == FILE) && typeLow >= sFileFilter) {
                    print2File(typeLow, tagHead.tag, tagHead.fileHead + body)
                }
            }
        }

        private fun processTagAndHead(tag: String?): TagHead {
            var tag = tag
            if (!sTagIsSpace && !sLogHeadSwitch) {
                tag = sGlobalTag
            } else {
                val stackTrace = Throwable().stackTrace
                var targetElement = stackTrace[3]
                var fileName = targetElement.fileName
                var className: String
                if (fileName == null) { // 混淆可能会导致获取为空 加-keepattributes SourceFile,LineNumberTable
                    className = targetElement.className
                    val classNameInfo = className.split("\\.".toRegex()).toTypedArray()
                    if (classNameInfo.size > 0) {
                        className = classNameInfo[classNameInfo.size - 1]
                    }
                    val index = className.indexOf('$')
                    if (index != -1) {
                        className = className.substring(0, index)
                    }
                    fileName = "$className.java"
                } else {
                    val index = fileName.indexOf('.') // 混淆可能导致文件名被改变从而找不到"."
                    className = if (index == -1) fileName else fileName.substring(0, index)
                }
                tag = if (isSpace(tag)) className else tag
                if (sLogHeadSwitch) {
                    val tName = Thread.currentThread().name
                    val head = Formatter()
                            .format("%s, %s(%s:%d)",
                                    tName,
                                    targetElement.methodName,
                                    fileName,
                                    targetElement.lineNumber)
                            .toString()
                    val fileHead = " [$head]: "
                    return if (sStackDeep <= 1) {
                        TagHead(tag, arrayOf(head), fileHead)
                    } else {
                        val consoleHead = arrayOfNulls<String>(Math.min(sStackDeep, stackTrace.size - 3))
                        consoleHead[0] = head
                        val spaceLen = tName.length + 2
                        val space = Formatter().format("%" + spaceLen + "s", "").toString()
                        var i = 1
                        val len = consoleHead.size
                        while (i < len) {
                            targetElement = stackTrace[i + 3]
                            consoleHead[i] = Formatter()
                                    .format("%s%s(%s:%d)",
                                            space,
                                            targetElement.methodName,
                                            targetElement.fileName,
                                            targetElement.lineNumber)
                                    .toString()
                            ++i
                        }
                        TagHead(tag, consoleHead, fileHead)
                    }
                }
            }
            return TagHead(tag, null, ": ")
        }

        private fun processBody(type: Int, vararg contents: Any): String {
            var body = NULL_TIPS
            if (contents != null) {
                if (contents.size == 1) {
                    val `object` = contents[0]
                    body = `object`.toString()
                    if (type == JSON) {
                        body = formatJson(body)
                    } else if (type == XML) {
                        body = formatXml(body)
                    }
                } else {
                    val sb = StringBuilder()
                    var i = 0
                    val len = contents.size
                    while (i < len) {
                        val content = contents[i]
                        sb.append(ARGS)
                                .append("[")
                                .append(i)
                                .append("]")
                                .append(" = ")
                                .append(content.toString() ?: NULL)
                                .append(LINE_SEP)
                        ++i
                    }
                    body = sb.toString()
                }
            }
            return body
        }

        private fun formatJson(json: String): String {
            var json = json
            try {
                if (json.startsWith("{")) {
                    json = JSONObject(json).toString(4)
                } else if (json.startsWith("[")) {
                    json = JSONArray(json).toString(4)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }

        private fun formatXml(xml: String): String {
            var xml = xml
            try {
                val xmlInput: Source = StreamSource(StringReader(xml))
                val xmlOutput = StreamResult(StringWriter())
                val transformer = TransformerFactory.newInstance().newTransformer()
                transformer.setOutputProperty(OutputKeys.INDENT, "yes")
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
                transformer.transform(xmlInput, xmlOutput)
                xml = xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">$LINE_SEP")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return xml
        }

        private fun print2Console(type: Int, tag: String?, head: Array<String?>?, msg: String) {
            printBorder(type, tag, true)
            printHead(type, tag, head)
            printMsg(type, tag, msg)
            printBorder(type, tag, false)
        }

        private fun printBorder(type: Int, tag: String?, isTop: Boolean) {
            if (sLogBorderSwitch) {
                Log.println(type, tag, if (isTop) TOP_BORDER else BOTTOM_BORDER)
            }
        }

        private fun printHead(type: Int, tag: String?, head: Array<String?>?) {
            if (head != null) {
                for (aHead in head) {
                    Log.println(type, tag, if (sLogBorderSwitch) LEFT_BORDER + aHead else aHead?:"")
                }
                if (sLogBorderSwitch) Log.println(type, tag, SPLIT_BORDER)
            }
        }

        private fun printMsg(type: Int, tag: String?, msg: String) {
            val len = msg.length
            val countOfSub = len / MAX_LEN
            if (countOfSub > 0) {
                var index = 0
                for (i in 0 until countOfSub) {
                    printSubMsg(type, tag, msg.substring(index, index + MAX_LEN))
                    index += MAX_LEN
                }
                if (index != len) {
                    printSubMsg(type, tag, msg.substring(index, len))
                }
            } else {
                printSubMsg(type, tag, msg)
            }
        }

        private fun printSubMsg(type: Int, tag: String?, msg: String) {
            if (!sLogBorderSwitch) {
                Log.println(type, tag, msg)
                return
            }
            val sb = StringBuilder()
            val lines = msg.split(LINE_SEP.toRegex()).toTypedArray()
            for (line in lines) {
                Log.println(type, tag, LEFT_BORDER + line)
            }
        }

        private fun print2File(type: Int, tag: String?, msg: String) {
            val now = Date(System.currentTimeMillis())
            val format = FORMAT.format(now)
            val date = format.substring(0, 5)
            val time = format.substring(6)
            val fullPath = (sDir
                    ?: sDefaultDir) + sFilePrefix + "-" + date + ".txt"
            if (!createOrExistsFile(fullPath)) {
                Log.e(tag, "log to $fullPath failed!")
                return
            }
            val sb = StringBuilder()
            sb.append(time)
                    .append(T[type - V])
                    .append("/")
                    .append(tag)
                    .append(msg)
                    .append(LINE_SEP)
            val content = sb.toString()
            if (sExecutor == null) {
                sExecutor = Executors.newSingleThreadExecutor()
            }
            sExecutor!!.execute {
                var bw: BufferedWriter? = null
                try {
                    bw = BufferedWriter(FileWriter(fullPath, true))
                    bw.write(content)
                    Log.d(tag, "log to $fullPath success!")
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(tag, "log to $fullPath failed!")
                } finally {
                    try {
                        bw?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        private fun createOrExistsFile(filePath: String): Boolean {
            val file = File(filePath)
            if (file.exists()) return file.isFile
            return if (!createOrExistsDir(file.parentFile)) false else try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        private fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }


    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}
