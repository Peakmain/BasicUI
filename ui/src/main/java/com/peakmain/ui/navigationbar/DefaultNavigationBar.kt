package com.peakmain.ui.navigationbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.peakmain.ui.R

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/26  下午 12:15
 * mail : 2726449200@qq.com
 * describe ：
 */
class DefaultNavigationBar internal constructor(builder: Builder?) : AbsNavigationBar<DefaultNavigationBar.Builder?>(builder) {
    private var mActionBar: ActionBar? = null
    private var mToolbar: Toolbar? = null
    override fun attachNavigationParams() {
        super.attachNavigationParams()
        val context = builder!!.mContext
        // 处理左边文字
        val leftView = findViewById<TextView>(R.id.tv_left)
        leftView!!.visibility = builder.mLeftVisible


        //处理标题
        val titleView = findViewById<TextView>(R.id.tv_title)
        titleView!!.visibility = builder.mTitleVisible
        if (context is AppCompatActivity) {
            mToolbar = findViewById<Toolbar>(R.id.view_root)
            context.setSupportActionBar(mToolbar)
            mToolbar!!.setNavigationOnClickListener(builder.mNavigationOnClickListener)
            try {
                mToolbar!!.setBackgroundColor(ContextCompat.getColor(builder.mContext, builder.mToolbarBackgroundColor))
            } catch (e: Exception) {
                mToolbar!!.setBackgroundColor(builder.mToolbarBackgroundColor)
            }
            mActionBar = context.supportActionBar
            if (mActionBar == null) {
                return
            }
            if (builder.mToolbarBackIcon != null) {
                mToolbar!!.navigationIcon = builder.mToolbarBackIcon
            }
            mActionBar!!.setDisplayShowTitleEnabled(builder.mShowTitle)
            mActionBar!!.setDisplayHomeAsUpEnabled(builder.mShowHomeAsUp)
        }
        //右边图片处理
        val rightView = findViewById<ImageView>(R.id.iv_right)
        rightView!!.visibility = builder.mRightViewVisible
        //右边图片设置资源
        if (builder.mRightResId != 0) {
            rightView.setImageResource(builder.mRightResId)
            val layoutParams = rightView.layoutParams
            layoutParams.height = builder.mRightResHeight
            layoutParams.width = builder.mRightResWidth
            rightView.layoutParams = layoutParams
        }
    }

    fun setHomeAsUpIndicator(@DrawableRes resId: Int) {
        if (mActionBar != null) {
            mActionBar!!.setHomeAsUpIndicator(resId)
        }
    }

    fun getActionBar(): ActionBar? {
        return mActionBar
    }

    /**
     * 是否显示返回按钮
     *
     * @param showHomeAsUp true显示返回按钮 false不返回
     */
    fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean): DefaultNavigationBar {
        if (mActionBar != null) {
            mActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp)
        }
        return this
    }

    /**
     * 是否显示返回按钮和是否显示toolbar自带的title
     *
     * @param id 标题栏背景颜色资源id
     */
    fun setToolbarBackgroundColor(@ColorRes id: Int): DefaultNavigationBar {
        if (mToolbar != null) {
            try {
                mToolbar!!.setBackgroundColor(ContextCompat.getColor(builder!!.mContext, id))
            } catch (e: Exception) {
                throw RuntimeException("Toolbar background color is vaild")
            }
        }
        return this
    }

    /**
     * 是否显示toolbar中默认自带的title
     *
     * @param showTitleAsUp true代表显示 false代表不显示
     */
    fun setDisplayShowTitleEnabled(showTitleAsUp: Boolean): DefaultNavigationBar {
        if (mActionBar != null) {
            mActionBar!!.setDisplayShowTitleEnabled(showTitleAsUp)
        }
        return this
    }

    /**
     * 设置返回按钮的点击事件
     */
    fun setNavigationOnClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        if (mToolbar != null) {
            mToolbar!!.setNavigationOnClickListener(onClickListener)
        }
        return this
    }

    /**
     * 设置左边文字
     */
    fun setLeftText(text: String?): DefaultNavigationBar {
        (findViewById<View>(R.id.tv_left) as TextView?)!!.text = text
        return this
    }

    /**
     * 设置左边文字的点击事件
     */
    fun setLeftClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.tv_left)!!.setOnClickListener(onClickListener)
        return this
    }

    /**
     * 设置左边文字颜色
     */
    fun setLeftTextColor(@ColorRes colorRes: Int): DefaultNavigationBar {
        (findViewById<View>(R.id.tv_left) as TextView?)!!.setTextColor(ContextCompat.getColor(builder!!.mContext, colorRes))
        return this
    }

    /**
     * 隐藏左边文字
     */
    fun hideLeftText(): DefaultNavigationBar {
        findViewById<View>(R.id.tv_left)!!.visibility = View.GONE
        return this
    }

    /**
     * 隐藏标题
     */
    fun hideTitleText(): DefaultNavigationBar {
        findViewById<View>(R.id.tv_title)!!.visibility = View.GONE
        return this
    }

    /**
     * 显示标题文字
     */
    fun setTitleText(title: String?): DefaultNavigationBar {
        (findViewById<View>(R.id.tv_title) as TextView?)!!.text = title
        return this
    }

    /**
     * 设置标题事件
     */
    fun setTitleClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.tv_title)!!.setOnClickListener(onClickListener)
        return this
    }

    /**
     * 设置标题颜色
     */
    fun setTitleTextColor(@ColorRes colorRes: Int): DefaultNavigationBar {
        (findViewById<View>(R.id.tv_title) as TextView?)!!.setTextColor(ContextCompat.getColor(builder!!.mContext, colorRes))
        return this
    }

    /**
     * 隐藏右边图片
     */
    fun hideRightView(): DefaultNavigationBar {
        findViewById<View>(R.id.iv_right)!!.visibility = View.GONE
        return this
    }

    /**
     * 设置右边图片的点击事件
     */
    fun setRightViewClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.iv_right)!!.setOnClickListener(onClickListener)
        return this
    }

    /**
     * 设置右边资源的图片
     *
     * @param rightResId 右边资源的id
     */
    fun setRightResId(rightResId: Int): DefaultNavigationBar {
        (findViewById<View>(R.id.iv_right) as ImageView?)!!.setImageResource(rightResId)
        return this
    }

    open class Builder(val context: Context?, parent: ViewGroup?) : AbsNavigationBar.Builder<Builder?>(context!!, R.layout.ui_defualt_navigation_bar, parent!!) {
        var mLeftVisible = View.VISIBLE
        private var mDefaultNavigationBar: DefaultNavigationBar? = null
        var mTitleVisible = View.VISIBLE

        //返回按钮的点击事件
        var mNavigationOnClickListener: View.OnClickListener? = null
        var mToolbarBackgroundColor = if (context != null) ContextCompat.getColor(context, R.color.ui_color_01a8e3) else 0
        var mShowHomeAsUp = false
        var mShowTitle = false
        var mRightViewVisible = View.VISIBLE

        //修改图片资源
        var mRightResId = 0

        //设置图片资源的高度和宽度
        var mRightResHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        var mRightResWidth = ViewGroup.LayoutParams.WRAP_CONTENT

        //左边返回键
        var mToolbarBackIcon: Drawable? = null
        fun setNavigationIcon(@DrawableRes resId: Int) {
            setNavigationIcon(if (context != null) AppCompatResources.getDrawable(context, resId) else null)
        }

        fun setNavigationIcon(@Nullable icon: Drawable?) {
            mToolbarBackIcon = icon
        }

        /**
         * 设置左边文字
         *
         * @param text 文字
         */
        fun setLeftText(text: CharSequence?): Builder {
            setText(R.id.tv_left, text)
            return this
        }

        fun setLeftClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.tv_left, onClickListener)
            return this
        }

        /**
         * 设置左边文字的颜色
         */
        fun setLeftTextColor(color: Int): Builder {
            setTextColor(R.id.tv_left, color)
            return this
        }

        /**
         * 隐藏左边文字
         */
        fun hideLeftText(): Builder {
            mLeftVisible = View.GONE
            return this
        }

        /**
         * 设置标题文字
         */
        fun setTitleText(text: CharSequence?): Builder {
            setText(R.id.tv_title, text)
            return this
        }

        /**
         * 设置标题文字的点击事件
         */
        fun setTitleClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.tv_title, onClickListener)
            return this
        }

        /**
         * 设置标题文字的颜色
         */
        fun setTitleTextColor(color: Int): Builder {
            setTextColor(R.id.tv_title, color)
            return this
        }

        /**
         * 隐藏标题文字
         */
        fun hideTitleText(): Builder {
            mTitleVisible = View.GONE
            return this
        }

        /**
         * 隐藏右边图片
         */
        fun hideRightView(): Builder {
            mRightViewVisible = View.GONE
            return this
        }

        fun showRightView(): Builder {
            mRightViewVisible = View.VISIBLE
            return this
        }

        /**
         * 设置右边图片的点击事件
         */
        fun setRightViewClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.iv_right, onClickListener)
            return this
        }

        /**
         * 是否显示返回按钮和是否显示toolbar自带的title
         *
         * @param id 标题栏背景颜色资源id
         */
        fun setToolbarBackgroundColor(@ColorRes id: Int): Builder {
            mToolbarBackgroundColor = id
            return this
        }

        /**
         * 是否显示返回按钮
         *
         * @param showHomeAsUp true显示返回按钮 false不返回
         */
        fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean): Builder {
            mShowHomeAsUp = showHomeAsUp
            return this
        }

        /**
         * 是否显示toolbar中默认自带的title
         *
         * @param showTitle true代表显示 false代表不显示
         */
        fun setDisplayShowTitleEnabled(showTitle: Boolean): Builder {
            mShowTitle = showTitle
            return this
        }

        /**
         * 设置返回按钮的点击事件
         */
        fun setNavigationOnClickListener(onClickListener: View.OnClickListener?): Builder {
            mNavigationOnClickListener = onClickListener
            return this
        }

        /**
         * 设置右边资源的图片
         *
         * @param rightResId 右边资源的id
         */
        fun setRightResId(rightResId: Int): Builder {
            mRightResId = rightResId
            return this
        }

        /**
         * 设置右边资源的图片
         *
         * @param rightResId 右边资源的id
         */
        fun setRightResId(rightResId: Int, rightResWidth: Int, rightResHeight: Int): Builder {
            mRightResId = rightResId
            mRightResWidth = rightResWidth
            mRightResHeight = rightResHeight
            return this
        }

        override fun create(): DefaultNavigationBar? {
            if (mDefaultNavigationBar == null) {
                mDefaultNavigationBar = DefaultNavigationBar(this)
            }
            return mDefaultNavigationBar
        }
    }
}