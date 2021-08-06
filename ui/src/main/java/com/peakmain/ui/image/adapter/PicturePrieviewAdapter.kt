package com.peakmain.ui.image.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bm.library.PhotoView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.peakmain.ui.adapter.BaseViewPagerAdapter
import com.peakmain.ui.image.PicturePreviewActivity
import com.peakmain.ui.image.config.PictureFileMimeType
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.image.gif.GifHelper
import com.peakmain.ui.imageLoader.ImageLoader
import com.peakmain.ui.utils.LogUtils

/**
 * author ：Peakmain
 * createTime：2021/4/1
 * mail:2726449200@qq.com
 * describe：图片预览adapter
 */
class PicturePrieviewAdapter(var context: Context, var data: MutableList<PictureFileInfo>) :
    BaseViewPagerAdapter() {
    var gifPlayerCallback:( (gifHelper:GifHelper, bitmap:Bitmap, imageView: ImageView, delay:Int) -> Unit)? =null
    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
        val photoView = PhotoView(context)
        val imageUrl = getImageUrl(position)
        photoView.enable()
        when {
            PictureFileMimeType.isHttp(imageUrl!!) -> {
                Glide.with(context).load(imageUrl)
                    .apply(RequestOptions())
                    .into(photoView)
            }
            PictureFileMimeType.isImageGif(imageUrl) -> {
                val gifHelper = GifHelper.load(imageUrl)
                if(gifHelper!=null){
                    val width = gifHelper.width
                    val height = gifHelper.height
                    LogUtils.i("${imageUrl}的gif图片的宽:$width, 高:$height")
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val delay = gifHelper.updateFrame(bitmap)
                    photoView?.setImageBitmap(bitmap)
                    if (gifPlayerCallback != null) {
                        gifPlayerCallback?.let { it(gifHelper,bitmap,photoView!!,delay) }
                    }
                }else{
                    Glide.with(context).asGif().load(imageUrl)
                            .apply(RequestOptions())
                            .into(photoView)
                }

            }
            else -> {
                Glide.with(context).asBitmap().load(imageUrl)
                    .apply(RequestOptions())
                    .into(photoView)
            }
        }
        photoView.setOnClickListener {
            (context as PicturePreviewActivity).closeActivity()
        }
        return photoView
    }

    private fun getImageUrl(position: Int): String? {
        return data[position].filePath
    }

    override fun getCount(): Int {
        return data.size
    }

    fun setGifPlayCallBack(gifPlayerCallback: ((gifHelper:GifHelper,bitmap:Bitmap,imageView:ImageView,delay:Int) -> Unit)?){
        this.gifPlayerCallback=gifPlayerCallback
    }
}