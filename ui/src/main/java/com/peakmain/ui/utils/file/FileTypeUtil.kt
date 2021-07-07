package com.peakmain.ui.utils.file

import android.content.Context
import com.peakmain.ui.R
import com.peakmain.ui.image.entry.PictureFileInfo
import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * author:Peakmain
 * createTime:2021/3/26
 * mail:2726449200@qq.com
 * describe：
 */
object FileTypeUtil {
    @JvmField
    val ALL_FOLDER_AND_FILES_FILTER = FileFilter { pathname -> !pathname.isHidden }

    /**
     * 读取文件列表
     *
     * @param files
     * @return
     */
    @JvmStatic
    fun getFileInfosFromFileArray(files: Array<File>?): List<PictureFileInfo> {
        val fileInfos: MutableList<PictureFileInfo> =
            ArrayList()
        if (files != null) {
            val length = files.size
            for (i in 0 until length) {
                val file = files[i]
                val fileInfo =
                        getFileInfoFromFile(file)
                fileInfos.add(fileInfo)
            }
        }
        return fileInfos
    }

    /**
     * 根据一个File文件获取FileInfo
     *
     */
    private fun getFileInfoFromFile(file: File?): PictureFileInfo {
        val fileInfo =
            PictureFileInfo()
        if (file != null && file.exists()) {
            fileInfo.fileName = file.name
            fileInfo.filePath = file.path
            fileInfo.isDirectory = file.isDirectory
            if (file.isDirectory) {
                fileInfo.fileSize = getNumFilesInFolder(fileInfo).toLong()
            } else {
                fileInfo.fileSize = file.length()
            }
            //获取文件名最后一个小数点的位置
            val lastDotIndex = file.name.lastIndexOf(".")
            if (lastDotIndex > 0) {
                val fileSuffix = file.name.substring(lastDotIndex + 1)
                fileInfo.suffix = fileSuffix
            }
        }
        return fileInfo
    }

    /**
     * 获取一个文件夹的大小
     *
     * @param fileInfo
     * @return
     */
    private fun getNumFilesInFolder(fileInfo: PictureFileInfo?): Int {
        if (fileInfo != null) {
            //如果不是一个文件夹
            if (!fileInfo.isDirectory) {
                return 0
            }
            val files =
                File(fileInfo.filePath).listFiles(ALL_FOLDER_AND_FILES_FILTER)
            return files?.size ?: 0
        }
        return 0
    }

    /**
     * 获取文件夹对应的icon
     * @param file
     * @return
     */
    fun getFileIconResource(
        context: Context,
        file: PictureFileInfo
    ): Int {
        return if (file.isDirectory) R.drawable.ui_ic_file_folder else getFileTypeImageId(
                context,
                file.fileName
        )
    }

    /**
     * 获取文件夹对应的icon
     * @param fileName
     * @return
     */
    fun getFileTypeImageId(mContext: Context, fileName: String?): Int {
        val id: Int
        id = if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_file_txt_suffix)
                )
        ) {
            R.drawable.ui_ic_file_txt
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_file_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_file
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_video_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_video
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_audio_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_audio
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_ppt_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_ppt
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_pdf_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_pdf
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_image_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_image
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_apk_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_apk
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_word_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_word
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_excel_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_excel
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_numbers_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_numbers
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_pages_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_pages
        } else if (checkSuffix(
                        fileName,
                        mContext.resources
                                .getStringArray(R.array.ui_zip_file_suffix)
                )
        ) {
            R.drawable.ui_ic_file_zip
        } else {
            R.drawable.ui_ic_file_other
        }
        return id
    }

    /**
     * 检查后缀格式
     * @param fileName
     * @param fileSuffix
     * @return
     */
    fun checkSuffix(
        fileName: String?,
        fileSuffix: Array<String>
    ): Boolean {
        val var3 = fileSuffix.size
        for (var4 in 0 until var3) {
            val suffix = fileSuffix[var4]
            if (fileName != null && fileName.toLowerCase().endsWith(suffix)) {
                return true
            }
        }
        return false
    }

    /**
     * 文件列表比较器
     */
    class FileNameComparator :
        Comparator<PictureFileInfo> {
        override fun compare(
                lhs: PictureFileInfo,
                rhs: PictureFileInfo
        ): Int {
            return if (!lhs.isDirectory && !rhs.isDirectory) {
                lhs.fileName!!.compareTo(rhs.fileName!!, ignoreCase = true)
            } else if (lhs.isDirectory == rhs.isDirectory) {
                lhs.fileName!!.compareTo(rhs.fileName!!, ignoreCase = true)
            } else {
                if (lhs.isDirectory) -1 else 1
            }
        }

        companion object {
            protected const val FIRST = -1
            protected const val SECOND = 1
        }
    }
}