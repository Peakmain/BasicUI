package com.peakmain.basicui.activity.home

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.peakmain.basicui.BuildConfig
import com.peakmain.basicui.R
import com.peakmain.ui.compress.ImageCompressUtils.Companion.with
import com.peakmain.ui.compress.OnCompressListener
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：
 */
class ImageCompressActivity : AppCompatActivity(), View.OnClickListener {
    private var mIvImage: ImageView? = null

    /**
     * 压缩
     */
    private lateinit var mBtCompress: Button
    private var mTvResult: TextView? = null
    private val mBitmap: Bitmap? = null
    private val mPath: String? = null
    private lateinit var mImageLists: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_compress)
        initView()
        initData()
    }

    protected fun initView() {
        mIvImage = findViewById(R.id.iv_image)
        mBtCompress = findViewById(R.id.bt_compress)
        mBtCompress.setOnClickListener(this)
        mTvResult = findViewById(R.id.tv_result)
    }

    protected fun initData() {
        mImageLists = ArrayList()
        val directory = Environment.getExternalStorageDirectory().toString() + "/截屏"
        val path1 = "$directory/1.jpg"
        val path2 = "$directory/2.jpg"
        val path3 = "$directory/3.jpg"
        mImageLists.add(path1)
        mImageLists.add(path2)
        mImageLists.add(path3)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_compress -> {
                //设置输出文件目录
                val directory = Environment.getExternalStorageDirectory().toString() + "/peakmain"
                with(this)
                        .load(mImageLists!!) //设置加载图片集合
                        .ignoreCompress(100) //设置忽略的图片大小单位是kb
                        .setQuality(90) //设置压缩质量
                        .setOutFileDir(directory) //设置输出文件目录
                        .setCompressListener(object : OnCompressListener {
                            override fun onStart() {
                                Log.e(BuildConfig.TAG, "开始压缩")
                            }

                            override fun onSuccess(list: List<String?>?) {
                                Log.e(BuildConfig.TAG, "压缩完成")
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