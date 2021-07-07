package com.peakmain.ui.image.config

import android.content.Context
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.peakmain.ui.R
import com.peakmain.ui.constants.BasicUIUtils
import com.peakmain.ui.image.entry.LocalMedia
import com.peakmain.ui.utils.file.FileTypeUtil
import java.io.File

/**
 * author:Peakmain
 * createTime:2021/3/25
 * mail:2726449200@qq.com
 * describe：文件格式类型
 */
object PictureFileMimeType {
    fun ofAll(): Int {
        return PictureConfig.TYPE_ALL
    }

    fun ofImage(): Int {
        return PictureConfig.TYPE_IMAGE
    }

    fun ofVideo(): Int {
        return PictureConfig.TYPE_VIDEO
    }

    fun ofAudio(): Int {
        return PictureConfig.TYPE_AUDIO
    }

    fun isPictureType(pictureType: String?): Int {
        when (pictureType) {
            "image/png", "image/PNG", "image/jpeg", "image/JPEG", "image/webp", "image/WEBP", "image/gif", "image/bmp", "image/GIF", "imagex-ms-bmp" -> return PictureConfig.TYPE_IMAGE
            "video/3gp", "video/3gpp", "video/3gpp2", "video/avi", "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/mpeg", "video/webm", "video/mp2ts" -> return PictureConfig.TYPE_VIDEO
            "audio/mpeg", "audio/x-ms-wma", "audio/x-wav", "audio/amr", "audio/wav", "audio/aac", "audio/mp4", "audio/quicktime", "audio/lamr", "audio/3gpp" -> return PictureConfig.TYPE_AUDIO
        }
        return PictureConfig.TYPE_IMAGE
    }

    /**
     * 是否是gif
     *
     * @param pictureType
     * @return
     */
    fun isGif(pictureType: String?): Boolean {
        when (pictureType) {
            "image/gif", "image/GIF" -> return true
        }
        return false
    }

    /**
     * 是否是gif
     *
     * @param path
     * @return
     */
    fun isImageGif(path: String): Boolean {
        if (!TextUtils.isEmpty(path)) {
            val lastIndex = path.lastIndexOf(".")
            val pictureType = path.substring(lastIndex, path.length)
            return (pictureType.startsWith(".gif")
                    || pictureType.startsWith(".GIF"))
        }
        return false
    }

    /**
     * 是否是PDF
     */
    fun isPdf(fileName: String): Boolean {
        return FileTypeUtil.checkSuffix(
                fileName,
                BasicUIUtils.application!!.resources
                        .getStringArray(R.array.ui_pdf_file_suffix)
        )
    }

    /**
     * 是否是apk
     */
    fun isApk(fileName: String): Boolean {
        return FileTypeUtil.checkSuffix(
                fileName,
                BasicUIUtils.application!!.resources
                        .getStringArray(R.array.ui_apk_file_suffix)
        )
    }

    /**
     * 判断是否是图片
     */
    fun isImage(path: String): Boolean {
        if (!TextUtils.isEmpty(path)) {
            val lastIndex = path.lastIndexOf(".")
            if (lastIndex <= 0) return false
            val pictureType = path.substring(lastIndex, path.length)
            return (pictureType.startsWith(".jpg")
                    || pictureType.startsWith(".png")
                    || pictureType.startsWith(".PNG")
                    || pictureType.startsWith(".jpeg")
                    || pictureType.startsWith(".JPEG")
                    || pictureType.startsWith(".webp")
                    || pictureType.startsWith(".WEBP")
                    || pictureType.startsWith(".gif")
                    || pictureType.startsWith(".bmp")
                    || pictureType.startsWith(".GIF"))
        }
        return false
    }

    /**
     * 是否是视频
     *
     * @param pictureType
     * @return
     */
    fun isVideo(pictureType: String?): Boolean {
        when (pictureType) {
            "video/3gp", "video/3gpp", "video/3gpp2", "video/avi", "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/mpeg", "video/webm", "video/mp2ts" -> return true
        }
        return false
    }

    /**
     * 是否是网络图片
     *
     * @param path
     * @return
     */
    fun isHttp(path: String): Boolean {
        if (!TextUtils.isEmpty(path)) {
            if (path.startsWith("http")
                    || path.startsWith("https")
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 判断文件类型是图片还是视频
     *
     * @param file
     * @return
     */
    fun fileToType(file: File?): String {
        if (file != null) {
            val name = file.name
            if (name.endsWith(".mp4") || name.endsWith(".avi")
                    || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")
            ) {
                return "video/mp4"
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                    || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
                    || name.endsWith(".bmp")
            ) {
                return "image/jpeg"
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac") || name.endsWith(".lamr")
            ) {
                return "audio/mpeg"
            }
        }
        return "image/jpeg"
    }


    fun mimeToEqual(p1: String?, p2: String?): Boolean {
        return isPictureType(p1) == isPictureType(p2)
    }

    fun createImageType(path: String?): String {
        try {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                val fileName = file.name
                val last = fileName.lastIndexOf(".") + 1
                val temp = fileName.substring(last, fileName.length)
                return "image/$temp"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "image/jpeg"
        }
        return "image/jpeg"
    }

    fun createVideoType(path: String?): String {
        try {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                val fileName = file.name
                val last = fileName.lastIndexOf(".") + 1
                val temp = fileName.substring(last, fileName.length)
                return "video/$temp"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "video/mp4"
        }
        return "video/mp4"
    }

    /**
     * Picture or video
     *
     * @return
     */
    fun pictureToVideo(pictureType: String): Int {
        if (!TextUtils.isEmpty(pictureType)) {
            if (pictureType.startsWith("video")) {
                return PictureConfig.TYPE_VIDEO
            } else if (pictureType.startsWith("audio")) {
                return PictureConfig.TYPE_AUDIO
            }
        }
        return PictureConfig.TYPE_IMAGE
    }

    /**
     * get Local video duration
     *
     * @return
     */
    fun getLocalVideoDuration(videoPath: String?): Float {
        var duration = 0f
        duration = try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(videoPath)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt().toFloat()
        } catch (e: Exception) {
            e.printStackTrace()
            return 0f
        }
        return if (duration > 0) duration / 1000 else 0f
    }

    /**
     * 是否是长图
     *
     * @param media
     * @return true 是 or false 不是
     */
    fun isLongImg(media: LocalMedia?): Boolean {
        if (null != media) {
            val width = media.width
            val height = media.height
            val h = width * 3
            return height > h
        }
        return false
    }

    /**
     * 获取图片后缀
     *
     * @param path
     * @return
     */
    fun getLastImgType(path: String): String {
        return try {
            val index = path.lastIndexOf(".")
            if (index > 0) {
                val imageType = path.substring(index, path.length)
                when (imageType) {
                    ".png", ".PNG", ".jpg", ".jpeg", ".JPEG", ".WEBP", ".bmp", ".BMP", ".webp" -> imageType
                    else -> ".png"
                }
            } else {
                ".png"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ".png"
        }
    }

    /**
     * 根据不同的类型，返回不同的错误提示
     *
     * @param mediaMimeType
     * @return
     */
    fun s(context: Context, mediaMimeType: Int): String {
        val ctx = context.applicationContext
        return when (mediaMimeType) {
            PictureConfig.TYPE_IMAGE -> ctx.getString(R.string.ui_picture_error)
            PictureConfig.TYPE_VIDEO -> ctx.getString(R.string.ui_picture_video_error)
            PictureConfig.TYPE_AUDIO -> ctx.getString(R.string.ui_picture_audio_error)
            else -> ctx.getString(R.string.ui_picture_error)
        }
    }

    const val JPEG = ".JPEG"
    const val PNG = ".png"
}