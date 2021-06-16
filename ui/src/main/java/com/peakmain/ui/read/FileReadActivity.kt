package com.peakmain.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.peakmain.ui.R
import com.peakmain.ui.image.config.PictureFileMimeType
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

    companion object {
        private const val FILE_READ_URL = "FILE_READ_URL"
        private const val FILE_READ_NAME = "FILE_READ_NAME"
        fun startActivity(
                context: Context,
                path: String
        ) {
            val intent = Intent(context, FileReadActivity::class.java)
            intent.putExtra(FILE_READ_URL, path)
            context.startActivity(intent)
        }

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

    private val name by lazy {
        intent.getStringExtra(FILE_READ_NAME)
    }
    private val path by lazy {
        intent.getStringExtra(FILE_READ_URL)
    }

    //  val storageDirectory = "${Environment.getExternalStorageDirectory()}/Android阿里巴巴规范.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_activity_file_read)
        mFileReadView = findViewById(R.id.ui_file_read)
        mTitleView = findViewById(R.id.ui_tv_title)
        if (path != null) {
            if (name != null) {
                mTitleView.text = name
            } else {
                val subPosition = path.lastIndexOf('/') + 1
                if (subPosition != -1) {
                    val name = path.substring(subPosition)
                    mTitleView.text = name
                } else {
                    mTitleView.text = path
                }
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

        if (PictureFileMimeType.isHttp(fileUri)) {
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
        } else {
            //直接预览
            mFileReadView.openFile(fileUri)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mFileReadView.onDestory()
    }

}