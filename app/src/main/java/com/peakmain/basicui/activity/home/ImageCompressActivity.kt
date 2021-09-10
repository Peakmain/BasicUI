package com.peakmain.basicui.activity.home

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.peakmain.basicui.BuildConfig
import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.compress.ImageCompressUtils.Companion.with
import com.peakmain.ui.compress.OnCompressListener
import com.peakmain.ui.image.PictureSelector
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.imageLoader.ImageLoader
import com.peakmain.ui.utils.FileUtils
import java.io.File
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
class ImageCompressActivity : BaseActivity(), View.OnClickListener {
    private var mIvImage: ImageView? = null

    /**
     * 压缩
     */
    private lateinit var mBtCompress: Button
    private var mTvResult: TextView? = null
    private lateinit var mImageLists: MutableList<String>


    override fun getLayoutId(): Int {
        return R.layout.activity_image_compress
    }

    override fun initView() {
        mIvImage = findViewById(R.id.iv_image)
        mBtCompress = findViewById(R.id.bt_compress)
        mBtCompress.setOnClickListener(this)
        mTvResult = findViewById(R.id.tv_result)
        mNavigationBuilder!!.setTitleText("图片压缩").create()
    }

    override fun initData() {
        mImageLists = ArrayList()
    }

    fun selectImageClick(view: View) {
        PictureSelector.create(this)
                .maxSelectNumber(1)
                .forResult(object : PictureFileResultCallback {
                    override fun onResult(result: ArrayList<PictureFileInfo>?) {
                        ImageLoader.instance?.displayImage(this@ImageCompressActivity, result!![0].filePath!!, mIvImage)
                        val filePath = result!![0].filePath!!
                        mImageLists.add(filePath)
                        mTvResult?.text = FileUtils.FormetFileSize(FileUtils.getFileSize(File(filePath)))
                    }

                })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_compress -> {
                //设置输出文件目录
                val directory = Environment.getExternalStorageDirectory().toString() + "/peakmain"
                with(this)
                        .load(mImageLists) //设置加载图片集合
                        .ignoreCompress(100) //设置忽略的图片大小单位是kb
                        .setQuality(90) //设置压缩质量
                        .setOutFileDir(directory) //设置输出文件目录
                        .setCompressListener(object : OnCompressListener {
                            override fun onStart() {
                                Log.e(BuildConfig.TAG, "开始压缩")
                            }

                            override fun onSuccess(list: List<String?>?) {
                                Log.e(BuildConfig.TAG, "压缩完成$list")
                                Log.e(BuildConfig.TAG, "文件是否存在${File(list?.get(0)).exists()}")
                                mTvResult?.text = FileUtils.FormetFileSize(FileUtils.getFileSize(File(list?.get(0))))
                            }

                            override fun onError(e: Throwable?) {
                                Log.e(BuildConfig.TAG, "压缩错误" + e!!.message)
                            }
                        }).startCompress()
            }
            else -> {
            }
        }
    }
}