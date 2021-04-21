package com.peakmain.ui.image.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.peakmain.ui.R
import com.peakmain.ui.image.PicturePreview
import com.peakmain.ui.image.PictureSelectorActivity
import com.peakmain.ui.image.`interface`.UpdateSelectListener
import com.peakmain.ui.image.config.PictureConfig
import com.peakmain.ui.image.config.PictureFileMimeType
import com.peakmain.ui.image.entry.SelectImageFileEntity
import com.peakmain.ui.image.entry.FileInfo
import com.peakmain.ui.image.entry.ImageEntity
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.image.fragment.FileListFragment
import com.peakmain.ui.read.FileReadActivity
import com.peakmain.ui.utils.*
import com.peakmain.ui.utils.FileTypeUtil.getFileIconResource
import com.peakmain.ui.utils.FileUtils.FormetFileSize

/**
 * author ：Peakmain
 * createTime：2021/3/26
 * mail:2726449200@qq.com
 * describe：
 */
class FileListAdapter(
        context: Context?,
        private val mSelectFileList: ArrayList<SelectImageFileEntity>,
        private val maxCount: Int,
        data: List<FileInfo?>
) : CommonRecyclerAdapter<FileInfo?>(
        context,
        data,
        R.layout.ui_item_file_list_adapter
) {
    var installApk: String? = ""
    override fun convert(
            holder: ViewHolder,
            item: FileInfo?
    ) {
        holder.setText(R.id.tv_file_name, item?.fileName)
        //判断如果是文件夹就显示文件数量，是文件则显示文件大小
        if (item!!.isDirectory) {
            holder.setVisibility(View.GONE, R.id.ui_file_select)
            holder.setText(
                    R.id.tv_file_detail,
                    String.format(
                            mContext?.getString(R.string.ui_files_numbers)!!,
                            item.fileSize
                    )
            )
        } else {
            val mFileSelect = holder.getView<ImageView>(R.id.ui_file_select)
            mFileSelect?.visibility = View.VISIBLE
            var tempData = SelectImageFileEntity(PictureConfig.FILE, item.filePath)
            if (!TextUtils.isEmpty(item.filePath) && PictureFileMimeType.isImage(item.filePath!!)) {
                tempData = SelectImageFileEntity(PictureConfig.IMAGE, item.filePath)
            }
            mFileSelect?.isSelected = mSelectFileList.contains(tempData)
            holder.setText(
                    R.id.tv_file_detail,
                    String.format(
                            mContext?.getString(R.string.ui_format_file_size)!!,
                            FormetFileSize(item.fileSize)
                    )
            )
            mFileSelect!!.setOnClickListener {
                //判断文件大小是否大于100M  1M=1024x1024B   因为1M=1024K 1K=1024B(字节)
                if (item.fileSize > FileListFragment.MAX_FILESIZE * 1024 * 1024) {
                    ToastUtils.showLong("无法选择大于${FileListFragment.MAX_FILESIZE}M的文件")
                } else {
                    var tempData = SelectImageFileEntity(PictureConfig.FILE, item.filePath)
                    if (PictureFileMimeType.isImage(item.filePath!!)) {
                        //是图片
                        tempData = SelectImageFileEntity(PictureConfig.IMAGE, item.filePath)
                    }

                    if (mSelectFileList.contains(tempData)) {
                        mSelectFileList.remove(tempData)
                    } else {
                        //判断是否达到最大
                        if (maxCount == mSelectFileList.size) {
                            ToastUtils.showLong(
                                    String.format(
                                            mContext?.getString(R.string.ui_picture_message_max_num)!!,
                                            maxCount
                                    )
                            )
                            return@setOnClickListener
                        }
                        if (PictureFileMimeType.isImage(item.filePath!!)) {
                            mSelectFileList.add(
                                    SelectImageFileEntity(
                                            PictureConfig.IMAGE,
                                            item.filePath!!
                                    )
                            )
                        } else {
                            if (!PictureFileMimeType.isPdf(item.fileName!!)) {
                                ToastUtils.showShort("暂不支持${item.fileName}格式")
                            } else {
                                mSelectFileList.add(
                                        SelectImageFileEntity(
                                                PictureConfig.FILE,
                                                item.filePath!!
                                        )
                                )
                            }

                        }


                    }
                    mListener?.selector()
                    (mContext as PictureSelectorActivity).updateSelectDataResult(mSelectFileList)
                    notifyItemChanged(holder.adapterPosition)
                }

            }
        }

        holder.setImageResource(
                R.id.iv_file_icon,
                getFileIconResource(mContext!!, item)
        )
        holder.setOnItemClickListener(View.OnClickListener {
            if (item.isDirectory) {
                val bundle = Bundle()
                bundle.putString(PictureSelectorActivity.directory, item.filePath)
                bundle.putSerializable(PictureSelectorActivity.SELECT_RESULT_KEY, mSelectFileList)
                (mContext as PictureSelectorActivity).showFragment(bundle)
            } else {
                if (PictureFileMimeType.isImage(item.filePath!!)) {
                    val images = java.util.ArrayList<ImageEntity?>()
                    val imageEntity = ImageEntity(item.filePath!!, item.fileName!!, 0, item.fileSize)
                    images.add(imageEntity)
                    PicturePreview.create(context = mContext!!)
                            .origin(images)
                            .showBottomView(false)
                            .showTitleLeftBack(true)
                            .showTitleRightIcon(false)
                            .forResult(null)
                } else if (PictureFileMimeType.isApk(item.filePath!!)) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val canInstall = mContext!!.packageManager.canRequestPackageInstalls()
                            this.installApk = item.filePath
                            if (!canInstall) {
                                val packageURI = Uri.parse("package:" + mContext!!.packageName)
                                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                               if(mInstallAppListener!=null){
                                   mInstallAppListener!!.installAppClick(intent)
                               }
                            }else{
                                AppUtils.installApp(item.filePath)
                            }
                        } else {
                            AppUtils.installApp(item.filePath)
                        }

                } else {
                    FileReadActivity.startActivity(mContext!!, item.filePath!!)
                }

            }
        })
    }

    var mInstallAppListener: InstanllAppListener? = null
    fun setInstallAppListener(installAppListener: InstanllAppListener) {
        this.mInstallAppListener=installAppListener
    }

    interface InstanllAppListener {
        fun installAppClick(intent: Intent)
    }

    private var mListener: UpdateSelectListener? = null
    fun setOnUpdateSelectListener(listener: UpdateSelectListener?) {
        mListener = listener
    }

    fun installApk(context: Context?) {
        if (installApk != null && installApk!!.isNotEmpty()) {
            AppUtils.installApp(installApk)
        }
    }
}