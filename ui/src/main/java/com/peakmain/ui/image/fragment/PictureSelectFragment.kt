package com.peakmain.ui.image.fragment

import android.app.Activity
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peakmain.ui.R
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.image.PicturePreview
import com.peakmain.ui.image.PictureSelectorActivity
import com.peakmain.ui.image.`interface`.PictureFileResultCallback
import com.peakmain.ui.image.`interface`.UpdateSelectListener
import com.peakmain.ui.image.adapter.ImageSelectorListAdapter
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureSelectionConfig
import com.peakmain.ui.image.entry.SelectImageFileEntity
import com.peakmain.ui.image.entry.ImageEntity
import java.io.File
import kotlin.collections.ArrayList

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
    private var mImageAdapter: ImageSelectorListAdapter? = null

    private var mResultList: ArrayList<SelectImageFileEntity>? = null

    private lateinit var mConfig: PictureSelectionConfig

    companion object {
        // 是否显示相机的EXTRA_KEY
        const val EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.ui_fragment_image_select, container, false)
        initView(view)

        // 初始化本地图片数据
        initImageList()
        return view
    }

    private fun initView(view: View) {
        mRvImageList =
            view.findViewById<View>(R.id.rv_image_list) as RecyclerView


        val bundle = arguments
        if (bundle != null) {
            mResultList =
                bundle.getSerializable(PictureSelectorActivity.SELECT_RESULT_KEY) as ArrayList<SelectImageFileEntity>
        }
        if (mResultList == null) {
            mResultList = ArrayList()
        }
        mConfig = PictureSelectionConfig.newInstance
    }


    override fun selector() {
        //告诉ImageSelectorActivity有选择新数据的变化
        (activity as PictureSelectorActivity).updateSelectDataResult(mResultList!!)
    }

    override fun openCamera(file: File?) {
        mTempFile = file
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri =
            FileProvider.getUriForFile(
                activity!!,
                    BasicUIUtils.application!!.packageName + ".fileProvider",
                file!!
            )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, ImageSelectorListAdapter.REQUEST_CAMERA)
    }

    /**
     * 初始化本地图片数据
     */
    private fun initImageList() {
        mRvImageList.layoutManager = GridLayoutManager(activity, 4)
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
        activity?.setResult(Activity.RESULT_OK, data)
        activity?.finish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageSelectorListAdapter.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            // notify system the image has change
            activity!!.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(mTempFile)
                )
            )
            mResultList!!.add(
                SelectImageFileEntity(
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
    private fun showListData(images: ArrayList<ImageEntity?>) {
        if (mImageAdapter == null) {
            mImageAdapter =
                ImageSelectorListAdapter(
                    activity!!,
                    mResultList!!,
                    mConfig.maxSelectNumber,
                    mConfig.selectionMode
                )
        }
        mImageAdapter!!.setData(images, mConfig.showCamera)
        mRvImageList.adapter = mImageAdapter
        mImageAdapter!!.setOnUpdateSelectListener(this)
        mImageAdapter?.setOnPicturePreviewClick(object :
            ImageSelectorListAdapter.PicturePreviewClick {
            override fun onPicturePreviewClick(
                position: Int,
                selectImages: ArrayList<SelectImageFileEntity>
            ) {
                gotoPicturePreviewActivity(images, position, selectImages)
            }

        })
    }

    private fun gotoPicturePreviewActivity(
            images: ArrayList<ImageEntity?>,
            position: Int,
            selectImages: ArrayList<SelectImageFileEntity>
    ) {
        PicturePreview.create(activity!!)
            .origin(images)
            .previewPosition(position)
            .selectImageFileList(selectImages)
            .showBottomView(true)
            .showTitleLeftBack(true)
            .showTitleRightIcon(true)
            .forResult(object : PictureFileResultCallback {
                override fun onResult(result: ArrayList<SelectImageFileEntity>?) {
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
                    activity,
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
                        ArrayList<ImageEntity?>()
                    data.moveToFirst()
                    // 不断的遍历循环
                    do {
                        val path =
                            data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        val name =
                            data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                        val dateTime =
                            data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))

                        // 判断文件是不是存在
                        if (!pathExist(path)) {
                            continue
                        }
                        // 封装数据对象
                        val image = ImageEntity(path, name, dateTime)
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
}