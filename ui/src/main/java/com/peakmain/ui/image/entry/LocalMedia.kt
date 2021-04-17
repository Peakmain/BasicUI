package com.peakmain.ui.image.entry

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * author:Peakmain
 * createTime:2021/3/25
 * mail:2726449200@qq.com
 * describe：本地媒体文件对象
 */
class LocalMedia : Parcelable {
    //原图文件地址
    var path: String? = null

    //压缩后的地址
    var compressPath: String? = null

    //裁剪后的地址
    var cropPath: String? = null

    //音频时长  预留
    var duration: Int = 0

    //是否被选中
    var isChecked = false

    //是否裁剪
    var isIcCrop = false

    //当前索引
    var position = 0

    //文件类型
    var mimeType = 0

    //图片格式类型
    private var pictureType: String? = null

    //是否压缩
    var isCompressed = false

    //图片宽度
    var width = 0

    //图片高度
    var height = 0

    //数量 样式预留 可选择图片的时候显示当前数量
    var num = 0

    constructor() {}
    constructor(
        path: String?,
        duration: Int,
        mimeType: Int,
        pictureType: String?
    ) {
        this.path = path
        this.duration = duration
        this.mimeType = mimeType
        this.pictureType = pictureType
    }

    constructor(
        path: String?,
        duration: Int,
        mimeType: Int,
        pictureType: String?,
        width: Int,
        height: Int
    ) {
        this.path = path
        this.duration = duration
        this.mimeType = mimeType
        this.pictureType = pictureType
        this.width = width
        this.height = height
    }

    constructor(
        path: String?, duration: Int,
        isChecked: Boolean, position: Int, num: Int, mimeType: Int
    ) {
        this.path = path
        this.duration = duration
        this.isChecked = isChecked
        this.position = position
        this.num = num
        this.mimeType = mimeType
    }

    fun getPictureType(): String? {
        if (TextUtils.isEmpty(pictureType)) {
            pictureType = "image/jpeg"
        }
        return pictureType
    }

    fun setPictureType(pictureType: String?) {
        this.pictureType = pictureType
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(path)
        dest.writeString(compressPath)
        dest.writeString(cropPath)
        dest.writeInt(duration)
        dest.writeByte(if (isChecked) 1.toByte() else 0.toByte())
        dest.writeByte(if (isIcCrop) 1.toByte() else 0.toByte())
        dest.writeInt(position)
        dest.writeInt(mimeType)
        dest.writeString(pictureType)
        dest.writeByte(if (isCompressed) 1.toByte() else 0.toByte())
        dest.writeInt(width)
        dest.writeInt(height)
        dest.writeInt(num)
    }

    protected constructor(`in`: Parcel) {
        path = `in`.readString()
        compressPath = `in`.readString()
        cropPath = `in`.readString()
        duration = `in`.readInt()
        isChecked = `in`.readByte().toInt() != 0
        isIcCrop = `in`.readByte().toInt() != 0
        position = `in`.readInt()
        mimeType = `in`.readInt()
        pictureType = `in`.readString()
        isCompressed = `in`.readByte().toInt() != 0
        width = `in`.readInt()
        height = `in`.readInt()
        num = `in`.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocalMedia?> = object : Parcelable.Creator<LocalMedia?> {
            override fun createFromParcel(source: Parcel): LocalMedia? {
                return LocalMedia(source)
            }

            override fun newArray(size: Int): Array<LocalMedia?> {
                return arrayOfNulls(size)
            }
        }
    }
}