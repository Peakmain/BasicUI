package com.peakmain.ui.image

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.peakmain.ui.R
import com.peakmain.ui.image.adapter.PicturePrieviewAdapter
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureFileMimeType
import com.peakmain.ui.image.config.PicturePreviewConfig
import com.peakmain.ui.image.config.PictureSelectionConfig
import com.peakmain.ui.image.entry.GifPlayerMessage
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.image.fragment.FileListFragment
import com.peakmain.ui.image.fragment.PictureSelectFragment
import com.peakmain.ui.utils.FileUtils
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.utils.network.HttpUtils
import com.peakmain.ui.utils.network.callback.DownloadCallback
import java.io.File
import java.util.*


/**
 * author ：Peakmain
 * createTime：2021/3/25
 * mail:2726449200@qq.com
 * describe：
 */
internal class PicturePreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var mTvNumber: TextView
    private lateinit var mViewPager: ViewPager
    private lateinit var mTvTagNumber: TextView
    private lateinit var mTvComplete: TextView
    private lateinit var mIvSelect: ImageView
    private lateinit var mIvDownload: ImageView
    private lateinit var mIvClose: ImageView
    private lateinit var mLlComplete: LinearLayout
    private var mAllImageList: ArrayList<PictureFileInfo>? = null
    private lateinit var mConfig: PictureSelectionConfig
    private lateinit var mPreviewConfig: PicturePreviewConfig

    //来自网络的所有图片的集合
    private var mAllUrlDataList: ArrayList<String>? = null

    //已经选择的图片集合
    private var mSelectImageList: ArrayList<PictureFileInfo>? = null

    private var currentPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_activity_picture_preview)
        initView()
        getIntentResult()
        initData()
        initListener()
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val gifPlayCallback: GifPlayerMessage = msg.obj as GifPlayerMessage
            val gifHelper = gifPlayCallback.gifHelper
            val bitmap = gifPlayCallback.bitmap
            val delay = gifHelper.updateFrame(bitmap)
            gifPlayCallback.imageView.setImageBitmap(bitmap)
            val message = Message()
            message.what = msg.what
            message.obj = GifPlayerMessage(gifHelper, bitmap, gifPlayCallback.imageView, delay)
            sendMessageDelayed(message, delay.toLong())
        }
    }

    private fun initListener() {
        findViewById<View>(R.id.ui_left_back).setOnClickListener {
            //关闭
            mPreviewConfig.mResultCallBack = null
            closeActivity()
        }
        mIvSelect.setOnClickListener {
            selectImageClick()
        }
        mTvComplete.setOnClickListener {
            closeActivity()
        }
        mIvClose.setOnClickListener {
            finish()
            overridePendingTransition(0, R.anim.ui_activity_exit_transition_anim)
        }
        mIvDownload.setOnClickListener {
            //下载的点击事件
            if (mAllImageList != null) {
                val imageEntity = mAllImageList!![currentPosition]
                val fileUri = imageEntity.filePath
                if (!TextUtils.isEmpty(fileUri) && PictureFileMimeType.isHttp(fileUri!!)) {
                    val filePath: String =
                            FileUtils.getDownloadFolderPath() + fileUri.substring(fileUri.lastIndexOf("/") + 1)
                    Log.e("TAG", "start=====$filePath")
                    if (FileUtils.isFileExists(File(filePath))) {
                        //文件已经存在
                        ToastUtils.showLong("该图片在${filePath}中已存在....")
                    } else {
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
            }
        }
    }

    private fun initData() {
        if (mAllUrlDataList!!.size > 0) {
            //将所有的网络图片转成ImageEntity
            mAllUrlDataList!!.forEach {
                val imageEntity = PictureFileInfo()
                imageEntity.filePath = it
                mAllImageList!!.add(imageEntity)
            }
            mLlComplete.visibility = View.GONE
        }
        mIvSelect.isSelected = mAllImageList!![currentPosition].isSelect
        if (!TextUtils.isEmpty(mPreviewConfig.titlePrefix)) {
            mTvNumber.text =
                    "${mPreviewConfig.titlePrefix}${currentPosition + 1} / ${mAllImageList?.size}"
        } else {
            mTvNumber.text = "${currentPosition + 1} / ${mAllImageList?.size}"
        }
        val adapter = PicturePrieviewAdapter(this, mAllImageList!!)
        mViewPager.adapter = adapter
        mViewPager.currentItem = currentPosition
        mViewPager.addOnPageChangeListener(this)
        updateSelectText()
        adapter.setGifPlayCallBack { gifHelper, bitmap, imageView, delay ->
            val message = Message()
            message.what = 100
            message.obj = GifPlayerMessage(gifHelper, bitmap, imageView, delay)
            mHandler.sendMessageDelayed(message, delay.toLong())
        }
    }

    private fun getIntentResult() {
        mAllImageList = mPreviewConfig.imageFileLists as ArrayList<PictureFileInfo>?
        if (mAllImageList == null) {
            mAllImageList = ArrayList()
        }
        val mSelectImageSerializable = mPreviewConfig.selectImageFileLists
        if (mSelectImageSerializable != null) {
            mSelectImageList =
                    mSelectImageSerializable
        }
        if (mSelectImageList == null) {
            mSelectImageList = ArrayList()
        }

        currentPosition = mPreviewConfig.previewPosition

        mAllUrlDataList = mPreviewConfig.urlLists
        if (mAllUrlDataList == null) {
            mAllUrlDataList = ArrayList()
        }
    }

    private fun selectImageClick() {
        val currentImageEntity = mAllImageList!![currentPosition]
        if (currentPosition > -1) {
            if (currentImageEntity.fileSize > FileListFragment.MAX_FILESIZE * 1024 * 1024) {
                ToastUtils.showLong("无法选择大于${FileListFragment.MAX_FILESIZE}M的文件")
            } else {

                if (currentImageEntity.isSelect) {
                    //被选中则移除
                    currentImageEntity.isSelect = false
                    mSelectImageList!!.remove(
                            PictureFileInfo(
                                    PictureConfig.IMAGE,
                                    currentImageEntity.filePath
                            )
                    )
                    updateSelectText()
                    mIvSelect.isSelected = false
                } else {
                    //没有被选中就添加
                    if (mSelectImageList?.size == mConfig.maxSelectNumber) {
                        ToastUtils.showShort(
                                getString(R.string.ui_picture_message_max_num),
                                mConfig.maxSelectNumber
                        )
                        return
                    }
                    currentImageEntity.isSelect = true
                    mSelectImageList!!.add(
                            PictureFileInfo(
                                    PictureConfig.IMAGE,
                                    currentImageEntity.filePath
                            )
                    )
                    updateSelectText()
                    mIvSelect.isSelected = true
                }
                if (mPreviewConfig.mResultCallBack != null) {
                    mPreviewConfig.mResultCallBack!!.onResult(mSelectImageList)
                }
            }

        }
    }

    private fun initView() {
        window.decorView.setBackgroundColor(
                ContextCompat.getColor(
                        this,
                        android.R.color.background_dark
                )
        )
        mViewPager = findViewById(R.id.ui_view_pager)
        mTvNumber = findViewById(R.id.ui_tv_number)
        mTvTagNumber = findViewById(R.id.ui_tv_img_num)
        mTvComplete = findViewById(R.id.ui_tv_complete)
        mIvSelect = findViewById(R.id.ui_picture_select)
        mLlComplete = findViewById(R.id.ui_ll_complete)
        mIvDownload = findViewById(R.id.ui_iv_download)
        mIvClose = findViewById(R.id.ui_iv_close)
        mConfig = PictureSelectionConfig.newInstance
        mPreviewConfig = PicturePreviewConfig.newInstance

        findViewById<View>(R.id.ui_left_back).visibility =
                if (mPreviewConfig.showTitleLeftBack) View.VISIBLE else View.INVISIBLE
        mIvSelect.visibility =
                if (mPreviewConfig.showTitleRightIcon) View.VISIBLE else View.INVISIBLE
        mLlComplete.visibility = if (mPreviewConfig.showBottomView) View.VISIBLE else View.INVISIBLE
        mIvClose.visibility = if (mPreviewConfig.showClose) View.VISIBLE else View.INVISIBLE
        mIvDownload.visibility = if (mPreviewConfig.showDownload) View.VISIBLE else View.INVISIBLE
    }

    private fun updateSelectText() {
        if (mSelectImageList!!.size > 0) {
            mTvTagNumber.text = mSelectImageList!!.size.toString()
            mTvTagNumber.visibility = View.VISIBLE
            mTvComplete.apply {
                text = "已选择"
                isSelected = true
                isEnabled = true
            }

        } else {
            mTvComplete.apply {
                text = "请选择"
                isSelected = false
                isEnabled = false
            }
            mTvTagNumber.visibility = View.INVISIBLE
        }
    }


    override fun onPageSelected(position: Int) {
        this.currentPosition = position
        if (mAllImageList != null && mAllImageList!!.size > 0) {
            if (!TextUtils.isEmpty(mPreviewConfig.titlePrefix)) {
                mTvNumber.text =
                        "${mPreviewConfig.titlePrefix}${position + 1} / ${mAllImageList?.size}"
            } else {
                mTvNumber.text = "${position + 1} / ${mAllImageList?.size}"
            }
            mIvSelect.isSelected = mAllImageList!![position].isSelect
        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    fun closeActivity() {
        if (mPreviewConfig.mResultCallBack != null) {
            mPreviewConfig.mResultCallBack!!.onResult(mSelectImageList)
        }
        finish()
        overridePendingTransition(
                0,
                R.anim.ui_activity_exit_transition_anim
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(100)
        mHandler = Handler()
    }
}