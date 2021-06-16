package com.peakmain.ui.utils

import android.graphics.drawable.Drawable
import android.text.*
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt


/**
 * author ：Peakmain
 * createTime：2021/3/23
 * mail:2726449200@qq.com
 * describe：文本工具类
 */
class TextUtils private constructor() {

    companion object {
        /**
         *
         * 设置拆分的文字颜色（flags：默认包括起始位置）
         */
        fun clipTextColor(
                text: String,
                @ColorInt color: Int,
                start: Int,
                end: Int
        ): SpannableString {
            return clipTextColor(text, color, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        fun clipTextColor(
            text: String,
            @ColorInt color: Int,
            start: Int,
            end: Int,
            flags: Int
        ): SpannableString {
            val foregroundColorSpan = ForegroundColorSpan(color)
            val spannableString = SpannableString(text)
            spannableString.setSpan(foregroundColorSpan, start, end, flags)
            return spannableString
        }
    }

    class Builder(private val textView: TextView) {

        private var mText: CharSequence = ""
        private var mStart = 0
        private var mClickableSpanList = SparseArray<OnClickableSpan>()

        data class DataBean(
            @ColorInt var clipColor: Int,
            var start: Int,
            var end: Int,
            var flags: Int
        ) {
            var mStyle: Int = 0
            var mDip: Boolean = true
            var mAbsoluteTextSize: Int = 0
            var mRelativeTextSize: Float = 0f
            var mDrawable: Drawable? = null
            var isShowUnderLine: Boolean = false
            var isShowStrikethrough: Boolean = false
            var mBackgroundColor: Int = 0

        }

        //存放start和end
        private var mSparseArray: SparseArray<DataBean> = SparseArray()


        fun setText(text: CharSequence): Builder {
            this.mText = text
            return this
        }

        fun setClipText(@ColorInt clipColor: Int, start: Int, end: Int): Builder {
            //默认是[start,end)
            this.setClipText(clipColor, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            return this
        }

        /**
         * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE==(start,end)
         * Spanned.SPAN_INCLUSIVE_EXCLUSIVE==[start,end)
         * Spanned.SPAN_EXCLUSIVE_INCLUSIVE==(start,end]
         * Spanned.SPAN_INCLUSIVE_INCLUSIVE==[start,end]
         */

        fun setClipText(@ColorInt clipColor: Int, start: Int, end: Int, flags: Int): Builder {
            this.mStart = start
            if (!mSparseArray.containsKey(start) || mSparseArray.get(start) == null) {
                val dataBean = DataBean(clipColor, start, end, flags)
                mSparseArray.put(start, dataBean)
            } else {
                //已经有了更新就好
                mSparseArray[start] = DataBean(clipColor, start, end, flags)
            }
            return this
        }

        /**
         *设置高亮字体样式，如粗体
         */
        fun setStyleSpan(style: Int): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.mStyle = style
            return this
        }

        fun setBackgroundColor(@ColorInt color: Int): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.mBackgroundColor = color
            return this
        }

        /**
         * 是否显示删除线，默认不显示
         */
        fun showStrikethrough(isShowStrikethrough: Boolean): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.isShowStrikethrough = isShowStrikethrough
            return this
        }

        /**
         * 是否显示下划线，默认不显示
         */
        fun showUnderLine(isShowUnderLine: Boolean): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.isShowUnderLine = isShowUnderLine
            return this
        }

        fun setImageDrawable(drawable: Drawable, start: Int, end: Int): Builder {
            this.mStart = start
            if (!mSparseArray.containsKey(start) || mSparseArray.get(start) == null) {
                val dataBean = DataBean(0, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                dataBean.mDrawable = drawable
                mSparseArray.put(start, dataBean)
            } else {
                val dataBean = mSparseArray.get(start)
                dataBean.mDrawable = drawable
                //已经有了更新就好
                mSparseArray[start] = dataBean
            }
            return this
        }

        fun setOnClickableSpan(onClickableSpan: OnClickableSpan): Builder {
            val dataBean = mSparseArray.get(mStart)
            mClickableSpanList.put(dataBean.start, onClickableSpan)
            return this
        }

        /**
         *  参数表示为默认字体大小的多少倍
         *  0.5则表示默认字体的0.5倍
         */
        fun setRelativeTextSize(textSize: Float): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.mRelativeTextSize = textSize
            return this
        }

        /**
         * 设置字体的绝对大小
         */
        fun setAbsoluteTextSize(textSize: Int, dip: Boolean): Builder {
            val dataBean = mSparseArray.get(mStart)
            dataBean.mAbsoluteTextSize = textSize
            dataBean.mDip = dip
            return this
        }

        fun setAbsoluteTextSize(textSize: Int): Builder {
            return setAbsoluteTextSize(textSize, true)
        }

        private fun setSpan(
            spannableString: SpannableString,
            what: Any,
            dataBean: DataBean
        ) {
            spannableString.setSpan(what, dataBean.start, dataBean.end, dataBean.flags)
        }

        fun create() {
            var spannableString = SpannableString(mText)
            if (!TextUtils.isEmpty(mText)) {
                mSparseArray.forEach { key, value ->
                    setTextStyle(spannableString, value)
                }
                textView.text = spannableString
            } else {
                this.mText = textView.text.toString()
                spannableString = SpannableString(mText)
                if (!TextUtils.isEmpty(this.mText)) {
                    mSparseArray.forEach { key, value ->
                        setTextStyle(spannableString, value)
                    }
                    textView.text = spannableString
                }
            }
        }

        private fun setTextStyle(
            spannableString: SpannableString,
            dataBean: DataBean
        ) {
            textView.movementMethod = LinkMovementMethod.getInstance()
            val clipColor = dataBean.clipColor
            if (dataBean.mDrawable != null) {
                setSpan(spannableString, ImageSpan(dataBean.mDrawable!!), dataBean)
            }
            if (clipColor != 0) {
                setSpan(spannableString, ForegroundColorSpan(clipColor), dataBean)
                setSpan(spannableString, clickableSpan(dataBean), dataBean)
                if (dataBean.mStyle != 0) {
                    setSpan(spannableString, StyleSpan(dataBean.mStyle), dataBean)
                }
                if (dataBean.mRelativeTextSize != 0f) {
                    setSpan(spannableString, RelativeSizeSpan(dataBean.mRelativeTextSize), dataBean)
                }
                if (dataBean.mAbsoluteTextSize != 0) {
                    setSpan(
                        spannableString,
                        AbsoluteSizeSpan(dataBean.mAbsoluteTextSize, dataBean.mDip),
                        dataBean
                    )
                }
                if (dataBean.mBackgroundColor != 0) {
                    setSpan(
                        spannableString,
                        BackgroundColorSpan(dataBean.mBackgroundColor),
                        dataBean
                    )
                }

            }

        }

        private fun clickableSpan(dataBean: DataBean): ClickableSpan {
            return object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val onClickableSpan = mClickableSpanList.get(dataBean.start)
                    onClickableSpan.onClickableSpan(widget)
                }

                override fun updateDrawState(ds: TextPaint) {
                    //super.updateDrawState(ds)
                    //取消下划线
                    ds.isUnderlineText = dataBean.isShowUnderLine
                    ds.color
                }

            }
        }
    }


}


/**
 * 高亮文字的点击事件
 */
interface OnClickableSpan {
    fun onClickableSpan(view: View)
}
