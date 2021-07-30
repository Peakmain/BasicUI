package com.peakmain.ui.utils.network

import android.content.Context
import com.peakmain.ui.utils.HandlerUtils.runOnUiThread
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.OkHttpManager.okHttpClient
import com.peakmain.ui.utils.network.body.ExMultipartBody
import com.peakmain.ui.utils.network.callback.DownloadCallback
import com.peakmain.ui.utils.network.callback.EngineCallBack
import com.peakmain.ui.utils.network.callback.ProgressEngineCallBack
import com.peakmain.ui.utils.network.callback.UploadProgressListener
import com.peakmain.ui.utils.network.download.DownloadDispatcher
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.net.URLConnection

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：okhttp的网络引擎
 */
class OkHttpEngine : IHttpEngine {
    private var mOkHttpClient: OkHttpClient? = null
    private var mUrl: String = ""
    fun setOkHttpClient(okHttpClient: OkHttpClient?) {
        mOkHttpClient = okHttpClient
    }

    override fun get(context: Context, url: String, params: LinkedHashMap<String, Any>, callBack: EngineCallBack) {
        mUrl = HttpUtils.jointParams(url, params)
        LogUtils.e("Get请求路径：$mUrl")
        val requestBuilder = Request.Builder().url(mUrl).tag(context)
        //可以省略，默认是GET请求
        val request = requestBuilder.build()
        mOkHttpClient!!.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread(Runnable { callBack.onError(e) })
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val resultJson = response.body()!!.string()
                LogUtils.e("Get返回结果：$resultJson")
                runOnUiThread(Runnable { callBack.onSuccess(resultJson) })
            }
        })
    }

    override fun post(context: Context, url: String, params: LinkedHashMap<String, Any>, callBack: EngineCallBack) {
        mUrl = url
        val requestBody = appendBody(params)
        val request = Request.Builder()
                .url(mUrl)
                .tag(context)
                .post(requestBody)
                .build()
        mOkHttpClient!!.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        runOnUiThread(Runnable { callBack.onError(e) })
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        // 这个 两个回掉方法都不是在主线程中
                        val result = response.body()!!.string()
                        LogUtils.e("Post返回结果：$result")
                        runOnUiThread(Runnable { callBack.onSuccess(result) })
                    }
                }
        )
    }

    /**
     * 上传文件
     *
     * @param context  上下文
     * @param url      方法的url
     * @param file     上传的文件
     * @param callBack 回掉
     */
    override fun uploadFile(context: Context, url: String, file: File, callBack: ProgressEngineCallBack) {
        LogUtils.e("Upload请求的路径:$url")
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("platform", "android")
        builder.addFormDataPart("file", file.name, RequestBody
                .create(MediaType.parse(guessMimeType(file.absolutePath)), file))
        val body = ExMultipartBody(builder.build(), object : UploadProgressListener {
            override fun onProgress(total: Long, current: Long) {
                runOnUiThread(Runnable { callBack.onProgress(total, current) })
            }
        })
        val request = Request.Builder().url(url).post(body).build()
        val call = mOkHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread(Runnable { callBack.onError(e) })
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body()!!.string()
                LogUtils.e("Upload返回结果：$result")
                runOnUiThread(Runnable { callBack.onSuccess(result) })
            }
        })
    }

    /**
     * 单线程下载
     *
     * @param context  上下文
     * @param url      下载的URL
     * @param outFile  保存文件的目录
     * @param callback 下载的回掉
     */
    override fun downloadSingleManager(context: Context, url: String, outFile: File, callback: DownloadCallback) {
        val request = Request.Builder().url(url).build()
        val call = mOkHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread(Runnable { callback.onFailure(e) })
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (outFile == null) {
                    throw NullPointerException("File cannot be empty")
                }
                val inputStream = response.body()!!.byteStream()
                val outputStream = FileOutputStream(outFile)
                var len = 0
                val buffer = ByteArray(1024 * 1024)
                val contentLength = response.body()!!.contentLength()
                var currentSize = 0
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                    currentSize += len
                    val finalCurrentSize = currentSize
                    runOnUiThread(Runnable { callback.onProgress((finalCurrentSize * 1.0f / contentLength * 100).toInt()) })
                }
                inputStream.close()
                outputStream.close()
                runOnUiThread(Runnable { callback.onSucceed(outFile) })
            }
        })
    }

    override fun downloadMultiManager(context: Context, url: String, outFile: File, callback: DownloadCallback) {
        DownloadDispatcher.instance.startDownload(url, outFile, callback)
    }

    /**
     * 组装post请求参数body
     */
    protected fun appendBody(params: Map<String, Any>?): RequestBody {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
        addParams(builder, params)
        return builder.build()
    }

    // 添加参数
    private fun addParams(builder: MultipartBody.Builder, params: Map<String, Any>?) {
        val sb = StringBuffer("post请求:$mUrl").append("\n参数：")
        if (params != null && params.isNotEmpty()) {
            for (key in params.keys) {
                //builder.addFormDataPart(key, params[key].toString() + "")
                val value = params[key]
                if (value is File) {
                    // 处理文件 --> Object File
                    builder.addFormDataPart(key, value.name, RequestBody
                            .create(MediaType.parse(guessMimeType(value
                                    .absolutePath)), value))
                } else if (value is List<*>) {
                    // 代表提交的是 List集合
                    try {
                        val listFiles = value as List<File>
                        for (i in listFiles.indices) {
                            // 获取文件
                            val file = listFiles[i]
                            builder.addFormDataPart(key + i, file.name, RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .absolutePath)), file))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    builder.addFormDataPart(key, value.toString() + "")
                    sb.append("[$key=$value],")
                }
            }
        }
        LogUtils.e(sb.toString())
    }

    /**
     * 猜测文件类型
     */
    private fun guessMimeType(path: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        var contentTypeFor = fileNameMap.getContentTypeFor(path)
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream"
        }
        return contentTypeFor
    }

    init {
        if (mOkHttpClient == null) {
            mOkHttpClient = okHttpClient
        }
    }


}