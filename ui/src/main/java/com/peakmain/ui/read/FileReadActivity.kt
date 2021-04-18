package com.peakmain.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.utils.FileUtils
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.utils.network.HttpUtils
import com.peakmain.ui.utils.network.callback.DownloadCallback
import java.io.File

/**
 * author ：Peakmain
 * createTime：2021/4/16
 * mail:2726449200@qq.com
 * describe：
 */
class FileReadActivity : AppCompatActivity() {
    private lateinit var mFileReadView: FileReadView
    private lateinit var mTitleView: TextView
    private var connect = false

    companion object {
        private const val FILE_READ_URL = "FILE_READ_URL"
        private const val FILE_READ_NAME = "FILE_READ_NAME"
        fun startActivity(
            context: Context,
            path: String,
            name: String
        ) {
            val intent = Intent(context, FileReadActivity::class.java)
            intent.putExtra(FILE_READ_URL, path)
            intent.putExtra(FILE_READ_NAME, name)
            context.startActivity(intent)
        }
    }

    private val path by lazy {
        intent.getStringExtra(FILE_READ_URL)
    }
    private val name by lazy {
        intent.getStringExtra(FILE_READ_NAME)
    }

    //  val storageDirectory = "${Environment.getExternalStorageDirectory()}/Android阿里巴巴规范.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_activity_file_read)
        mFileReadView = findViewById(R.id.ui_file_read)
        mTitleView = findViewById(R.id.ui_tv_title)
        if (path != null) {
            if (!TextUtils.isEmpty(name)) {
                val title = name.substring(name.lastIndexOf("/")+1,name.lastIndexOf("."))
                mTitleView.text = title
            } else {
                mTitleView.text = "文章预览"
            }
            startPreview(path)
        } else {
            ToastUtils.showLong("文件不存在")
            finish()
        }
        findViewById<ImageView>(R.id.ui_left_back).setOnClickListener {
            finish()
        }
    }

    private fun startPreview(fileUri: String) {

        val filePath =
            FileUtils.getDownloadFolderPath() + fileUri.substring(fileUri.lastIndexOf("/") + 1)
        if (FileUtils.isFileExists(filePath)) {
            //直接预览
            mFileReadView.openFile(filePath)
        } else {
            //下载
            HttpUtils.with(this)
                    .url(fileUri)
                    .downloadSingle()
                    .file(File(filePath))
                    .exectureDownload(object : DownloadCallback {
                        override fun onFailure(e: Exception?) {
                            LogUtils.e(e!!.message)
                        }

                        override fun onSucceed(file: File?) {
                            ToastUtils.showShort("file下载完成")
                            LogUtils.e("文件保存的位置:" + file!!.absolutePath)
                        }

                        override fun onProgress(progress: Int) {
                            LogUtils.e("单线程下载file的进度:$progress")
                        }
                    })

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mFileReadView.onDestory()
    }

}