package com.peakmain.ui.image.fragment

import android.app.Activity
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.ui.R
import com.peakmain.ui.constants.PermissionConstants
import com.peakmain.ui.image.PicturePreview
import com.peakmain.ui.image.PictureSelectorActivity
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.`interface`.UpdateSelectListener
import com.peakmain.ui.image.adapter.PictureSelectorListAdapter
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureSelectionConfig
import com.peakmain.ui.image.entry.GifPlayerMessage
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.PermissionUtils
import com.peakmain.ui.utils.ToastUtils
import java.io.File

/**
 * author ：Peakmain
 * createTime：2021/3/26
 * mail:2726449200@qq.com
 * describe：图片选择
 */
internal class PictureSelectFragment : Fragment(), UpdateSelectListener {
    private lateinit var mRvImageList: RecyclerView

    // 拍照临时存放的文件
    private var mTempFile: File? = null

    // 图片显示的Adapter
    private var mImageAdapter: PictureSelectorListAdapter? = null

    private var mResultList: ArrayList<PictureFileInfo>? = null

    private lateinit var mConfig: PictureSelectionConfig

    companion object {
        const val MAX_FILESIZE = 20 //文件最大体积
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.ui_fragment_image_select, container, false)
        initView(view)
        PermissionUtils.request(fragment = this, requestCode = 123, permissions = PermissionConstants.getPermissions(PermissionConstants.STORAGE), block = object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                LogUtils.e("读写权限被授予了")
                // 初始化本地图片数据
                initImageList()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                ToastUtils.showLong("请开启读写权限")
                activity?.finish()
            }

        })
        return view
    }

    private fun initView(view: View) {
        mRvImageList =
                view.findViewById<View>(R.id.rv_image_list) as RecyclerView


        val bundle = arguments
        if (bundle != null) {
            mResultList =
                    bundle.getSerializable(PictureSelectorActivity.SELECT_RESULT_KEY) as ArrayList<PictureFileInfo>
        }
        if (mResultList == null) {
            mResultList = ArrayList()
        }
        mConfig = PictureSelectionConfig.newInstance
    }


    override fun selector() {
        //告诉ImageSelectorActivity有选择新数据的变化
        (context as PictureSelectorActivity).updateSelectDataResult(mResultList!!)
    }

    override fun openCamera(file: File?) {
        mTempFile = file
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri =
                FileProvider.getUriForFile(
                        context!!,
                        context!!.applicationContext.packageName + ".fileProvider",
                        file!!
                )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, PictureSelectorListAdapter.REQUEST_CAMERA)
    }

    /**
     * 初始化本地图片数据
     */
    private fun initImageList() {
        mRvImageList.layoutManager = GridLayoutManager(context, 4)
        activity!!.loaderManager.initLoader(
                PictureSelectorActivity.LOADER_TYPE,
                Bundle(),
                mLoaderCallback
        )
    }

    /**
     * 设置返回结果
     */
    private fun setResult() {
        val data = Intent()
        data.putExtra(PictureSelectorActivity.SELECT_RESULT_KEY, mResultList)
        (context as Activity).setResult(Activity.RESULT_OK, data)
        (context as Activity).finish()
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureSelectorListAdapter.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            // notify system the image has change
            context!!.sendBroadcast(
                    Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(mTempFile)
                    )
            )
            mResultList!!.add(
                    PictureFileInfo(
                            PictureConfig.IMAGE,
                            mTempFile!!.absolutePath
                    )
            )
            setResult()
        }
    }

    /**
     * 显示图片列表数据
     */
    private fun showListData(images: ArrayList<PictureFileInfo>?) {
        if (mImageAdapter == null) {
            mImageAdapter =
                    PictureSelectorListAdapter(
                            context!!,
                            mResultList!!,
                            mConfig.maxSelectNumber,
                            mConfig.selectionMode
                    )
        }
        mImageAdapter!!.setData(images, mConfig.showCamera)
        mRvImageList.adapter = mImageAdapter
        mImageAdapter!!.setOnUpdateSelectListener(this)
        mImageAdapter?.setOnPicturePreviewClick(object :
                PictureSelectorListAdapter.PicturePreviewClick {
            override fun onPicturePreviewClick(
                    position: Int,
                    selectImages: ArrayList<PictureFileInfo>
            ) {
                gotoPicturePreviewActivity(images, position, selectImages)
            }

        })
        mImageAdapter?.setGifPlayCallBack { gifHelper, bitmap, imageView, delay ->
            val message = Message()
            message.what = 100
            message.obj = GifPlayerMessage(gifHelper, bitmap, imageView, delay)
            mHandler.sendMessageDelayed(message, delay.toLong())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions)
    }

    private fun gotoPicturePreviewActivity(
            images: ArrayList<PictureFileInfo>?,
            position: Int,
            selectImages: ArrayList<PictureFileInfo>
    ) {
        PicturePreview.create(this)
                .origin(images)
                .previewPosition(position)
                .selectImageFileList(selectImages)
                .showBottomView(true)
                .showTitleLeftBack(true)
                .showTitleRightIcon(true)
                .forResult(object : PictureFileResultCallback {
                    override fun onResult(result: ArrayList<PictureFileInfo>?) {
                        mResultList = result
                        if (mResultList == null) {
                            mResultList = ArrayList()
                        }
                        selector()
                        mImageAdapter?.setSelectResult(mResultList)
                    }
                })

    }


    /**
     * 加载图片的CallBack
     */
    private val mLoaderCallback: LoaderManager.LoaderCallbacks<Cursor?> =
            object : LoaderManager.LoaderCallbacks<Cursor?> {
                private val IMAGE_PROJECTION = arrayOf(
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media._ID
                )

                override fun onCreateLoader(
                        id: Int,
                        args: Bundle
                ): Loader<Cursor?> {
                    return CursorLoader(
                            context,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                            arrayOf("image/jpeg", "image/png", "image/gif"),
                            IMAGE_PROJECTION[2] + " DESC"
                    )
                }

                override fun onLoadFinished(
                        loader: Loader<Cursor?>,
                        data: Cursor?
                ) {
                    // 如果有数据变量数据
                    if (data != null && data.count > 0) {
                        val images =
                                ArrayList<PictureFileInfo>()
                        data.moveToFirst()
                        // 不断的遍历循环
                        do {
                            val path =
                                    data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                            val name =
                                    data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                            val dateTime =
                                    data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                            val fileSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                            // 判断文件是不是存在
                            if (!pathExist(path)) {
                                continue
                            }
                            // 封装数据对象
                            val image = PictureFileInfo(path, name, dateTime, fileSize)
                            images.add(image)
                        } while (data.moveToNext())

                        // 显示列表数据
                        showListData(images)
                    }
                }

                /**
                 * 判断该路径文件是不是存在
                 */
                private fun pathExist(path: String): Boolean {
                    return if (!TextUtils.isEmpty(path)) {
                        File(path).exists()
                    } else false
                }

                override fun onLoaderReset(loader: Loader<Cursor?>) {}
            }

    override fun onDestroy() {
        super.onDestroy()
        PermissionUtils.onDestory()
        mHandler.removeMessages(100)
        mHandler = Handler()
    }
}