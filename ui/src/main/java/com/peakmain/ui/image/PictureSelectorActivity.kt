package com.peakmain.ui.image

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.image.adapter.ImageSelectorListAdapter
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureSelectionConfig
import com.peakmain.ui.image.entry.SelectImageFileEntity
import com.peakmain.ui.image.fragment.FileListFragment
import com.peakmain.ui.image.fragment.PictureSelectFragment
import com.peakmain.ui.utils.FileUtils.createTmpFile
import com.peakmain.ui.utils.PermissionConstants
import com.peakmain.ui.utils.PermissionUtils
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.widget.ShapeTextView
import com.xuantian.common.ui.helper.FragmentManagerHelper
import java.io.File
import java.io.IOException
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/3/25
 * mail:2726449200@qq.com
 * describe：
 */
internal class PictureSelectorActivity : AppCompatActivity() {
    private var mSelectNumTv: TextView? = null
    private lateinit var mImageFileTab: FrameLayout
    private lateinit var mFragmentHelper: FragmentManagerHelper
    private var mImageSelectFragment: PictureSelectFragment? = null
    private var mFileListFragement: FileListFragment? = null
    private lateinit var mUiTvPicture: TextView
    private lateinit var mUiTvTakePicture: TextView
    private lateinit var mUiTvFile: TextView

    // 拍照临时存放的文件
    private var mTempFile: File? = null

    //0代表相册 2代表文件
    private var mCurrentSelect = 0
    private var mFileListFragmentCount = 1
    private lateinit var mImageBackView: ImageView
    private lateinit var mLlBottomView: LinearLayout
    private var mResultList: ArrayList<SelectImageFileEntity>? = null
    private lateinit var mStvComplete: ShapeTextView
    private lateinit var mConfig: PictureSelectionConfig

    companion object {
        const val directory = "directory"

        // 加载所有的数据
        const val LOADER_TYPE = 0x0021

        //已经选择的数据集合key
        const val SELECT_RESULT_KEY = "SELECT_RESULT_KEY"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_activity_picture_selector)
        initView()
        initListener()
        getIntentResult()
        mFragmentHelper = FragmentManagerHelper(supportFragmentManager, R.id.ui_image_file_tab)
        mImageSelectFragment = PictureSelectFragment()
        val bundle = Bundle()
        bundle.putSerializable(SELECT_RESULT_KEY, mResultList)
        mImageSelectFragment?.arguments = bundle
        mFragmentHelper.addFragment(mImageSelectFragment!!)
        mImageBackView.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initView() {
        mSelectNumTv =
                findViewById<View>(R.id.select_num) as TextView
        mUiTvPicture = findViewById(R.id.ui_tv_picture)
        mUiTvTakePicture = findViewById(R.id.ui_tv_takePicture)
        mUiTvFile = findViewById(R.id.ui_tv_file)
        mImageFileTab = findViewById(R.id.ui_image_file_tab)
        mImageBackView = findViewById(R.id.ui_left_back)
        mLlBottomView = findViewById(R.id.ui_ll_bottom)
        mStvComplete = findViewById(R.id.ui_stv_complete)
        mConfig = PictureSelectionConfig.newInstance
        mStvComplete.isEnabled = false
        mStvComplete.setNormalBackgroundColor(ContextCompat.getColor(this, R.color.color_802F73F6))
    }

    private fun getIntentResult() {
        val intent = intent
        if (intent != null) {
            val serializableExtra = intent.getSerializableExtra(SELECT_RESULT_KEY)
            if (serializableExtra != null) {
                mResultList = serializableExtra as ArrayList<SelectImageFileEntity>
            }
            if (mResultList == null) {
                mResultList = ArrayList()
                mSelectNumTv?.text = "已选${0}/${mConfig.maxSelectNumber}"
            } else {
                updateSelectDataResult(mResultList!!)
            }

        }
    }

    override fun onBackPressed() {
        if (mCurrentSelect == 2 && --mFileListFragmentCount == 0) {
            val fm = supportFragmentManager

            for (i in 0 until fm.backStackEntryCount) {
                val entry =
                        fm.getBackStackEntryAt(i)
                val fragment =
                        fm.findFragmentByTag(entry.name)
                fragment?.onDestroy()
            }
            finish()
        } else {
            if (mCurrentSelect == 2 && mFileListFragmentCount == 1) {
                mLlBottomView.visibility = View.VISIBLE
                mImageBackView.setImageResource(R.drawable.ui_ic_baseline_close_24)
            }
            super.onBackPressed()
        }
    }

    private fun initListener() {
        mUiTvPicture.setOnClickListener {
            gotoImageSelectFragmentClick()
        }
        mUiTvTakePicture.setOnClickListener {
            openCameraClick()
        }
        mUiTvFile.setOnClickListener {
            gotoFileListFragmentClick()
        }
        mStvComplete.setOnClickListener {
            //数据返回出去
            onResult(mResultList)
        }
    }

    private fun onResult(mResultList: ArrayList<SelectImageFileEntity>?) {
        if (mResultList != null && mResultList.size > 0) {
            handleCallbackResult(mResultList)
            setResult(mResultList)
        }

    }

    private fun setResult(resultList: ArrayList<SelectImageFileEntity>?) {
        if (mConfig.mResultCallBack != null) {
            runOnUiThread {
                run {
                    mConfig.mResultCallBack!!.onResult(resultList)
                }
            }
        }
        finish()
    }

    /**
     * 回调之前可以做一些处理，比如图片压缩，裁剪等
     */
    public fun handleCallbackResult(resultList: ArrayList<SelectImageFileEntity>) {

    }

    private fun openCameraClick() {
        try {
            val tmpFile =
                    createTmpFile(this)
            openCamera(tmpFile)
        } catch (e: IOException) {
            e.printStackTrace()
            ToastUtils.showLong("相机打开失败")
        }
    }

    private fun gotoImageSelectFragmentClick() {
        if (mCurrentSelect == 0) {
            return
        }
        mCurrentSelect = 0
        mUiTvPicture.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        mUiTvPicture.alpha = 1f
        mUiTvFile.alpha = 0.5f
        mUiTvFile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        if (mImageSelectFragment == null) {
            mImageSelectFragment = PictureSelectFragment()
        }
        val bundle = Bundle()
        bundle.putSerializable(SELECT_RESULT_KEY, mResultList)
        mImageSelectFragment?.arguments = bundle
        mFragmentHelper.switchFragment(mImageSelectFragment!!)
    }

    private fun gotoFileListFragmentClick() {
        if (mCurrentSelect == 2)
            return
        mCurrentSelect = 2
        mUiTvPicture.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        mUiTvPicture.alpha = 0.5f
        mUiTvFile.alpha = 1f
        mUiTvFile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        if (mFileListFragement == null) {
            mFileListFragement = FileListFragment()
        }
        val bundle = Bundle()
        bundle.putSerializable(SELECT_RESULT_KEY, mResultList)
        bundle.putString(directory, Environment.getExternalStorageDirectory().path)
        mFileListFragement?.arguments = bundle
        mFragmentHelper.switchFragment(mFileListFragement!!)
    }


    private fun openCamera(file: File?) {
        mTempFile = file
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri =
                FileProvider.getUriForFile(
                        this,
                        applicationContext.packageName + ".fileProvider",
                        file!!
                )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, ImageSelectorListAdapter.REQUEST_CAMERA)
    }

    fun showFragment(bundle: Bundle?) {
        mFileListFragmentCount++
        mLlBottomView.visibility = View.GONE
        mImageBackView.setImageResource(R.drawable.ui_ic_left_back)
        val fragment = FileListFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().addToBackStack("" + fragment)
                .replace(R.id.ui_image_file_tab, fragment).commitAllowingStateLoss()
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImageSelectorListAdapter.REQUEST_CAMERA) {
                // notify system the image has change
                sendBroadcast(
                        Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(mTempFile)
                        )
                )
                if (mResultList!!.size >= mConfig.maxSelectNumber) {
                    ToastUtils.showLong(getString(R.string.ui_picture_message_max_num)!!,
                            mConfig.maxSelectNumber
                    )
                    finish()
                } else {
                    mResultList!!.add(
                            SelectImageFileEntity(
                                    PictureConfig.IMAGE,
                                    mTempFile!!.absolutePath
                            )
                    )
                    setResult(mResultList)
                }

            }
        }
    }

    fun updateSelectDataResult(list: ArrayList<SelectImageFileEntity>) {
        this.mResultList = list
        mSelectNumTv?.text = "已选${mResultList!!.size}/${mConfig.maxSelectNumber}"
        if (list.isEmpty()) {
            mStvComplete.apply {
                isEnabled = false
                setNormalBackgroundColor(
                        ContextCompat.getColor(
                                this@PictureSelectorActivity,
                                R.color.color_802F73F6
                        )
                )
                setTextColor(
                        ContextCompat.getColor(
                                this@PictureSelectorActivity,
                                R.color.color_9B9B9B
                        )
                )
            }
        } else {
            mStvComplete.apply {
                isEnabled = true
                mStvComplete.setNormalBackgroundColor(
                        ContextCompat.getColor(
                                this@PictureSelectorActivity,
                                R.color.color_2F73F6
                        )
                )
                setTextColor(
                        ContextCompat.getColor(
                                this@PictureSelectorActivity,
                                android.R.color.white
                        )
                )
            }
        }
    }
}