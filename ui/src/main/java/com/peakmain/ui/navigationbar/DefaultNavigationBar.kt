package com.peakmain.ui.navigationbar

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.peakmain.ui.R

class DefaultNavigationBar internal constructor(builder: Builder?) :
    AbsNavigationBar<DefaultNavigationBar.Builder?>(builder) {
    private var mActionBar: ActionBar? = null
    private var mToolbar: Toolbar? = null

    private val b: Builder
        get() = builder ?: throw IllegalStateException("Builder is null")

    override fun attachNavigationParams() {
        super.attachNavigationParams()
        val ctx = b.mContext

        findViewById<TextView>(R.id.tv_left)?.visibility = b.mLeftVisible
        findViewById<TextView>(R.id.tv_title)?.visibility = b.mTitleVisible

        if (ctx is AppCompatActivity) {
            mToolbar = findViewById<Toolbar>(R.id.view_root)
            ctx.setSupportActionBar(mToolbar)
            mToolbar?.setNavigationOnClickListener(b.mNavigationOnClickListener)
            try {
                mToolbar?.setBackgroundColor(
                    ContextCompat.getColor(b.mContext, b.mToolbarBackgroundColor)
                )
            } catch (e: Exception) {
                mToolbar?.setBackgroundColor(b.mToolbarBackgroundColor)
            }
            if (b.mToolbarBackIcon != null) {
                mToolbar?.navigationIcon = b.mToolbarBackIcon
            }
            mActionBar = ctx.supportActionBar ?: return
            when {
                b.mHomeAsUpIndicator != null -> mActionBar?.setHomeAsUpIndicator(b.mHomeAsUpIndicator!!)
                b.mHomeAsUpIndicatorDrawable != null -> mActionBar?.setHomeAsUpIndicator(b.mHomeAsUpIndicatorDrawable)
            }
            mActionBar?.setDisplayShowTitleEnabled(b.mShowTitle)
            mActionBar?.setDisplayHomeAsUpEnabled(b.mShowHomeAsUp)
        }

        findViewById<ImageView>(R.id.iv_right)?.let { rightView ->
            rightView.visibility = b.mRightViewVisible
            if (b.mRightResId != 0) {
                rightView.setImageResource(b.mRightResId)
                rightView.layoutParams = rightView.layoutParams.apply {
                    height = b.mRightResHeight
                    width = b.mRightResWidth
                }
            }
        }
    }

    fun setHomeAsUpIndicator(@DrawableRes resId: Int): DefaultNavigationBar {
        mActionBar?.setHomeAsUpIndicator(resId)
        return this
    }

    fun setHomeAsUpIndicator(indicator: Drawable): DefaultNavigationBar {
        mActionBar?.setHomeAsUpIndicator(indicator)
        return this
    }

    fun getActionBar(): ActionBar? = mActionBar

    fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean): DefaultNavigationBar {
        mActionBar?.setDisplayHomeAsUpEnabled(showHomeAsUp)
        return this
    }

    fun setToolbarBackgroundColor(@ColorRes id: Int): DefaultNavigationBar {
        mToolbar?.let { toolbar ->
            try {
                toolbar.setBackgroundColor(ContextCompat.getColor(b.mContext, id))
            } catch (e: Exception) {
                throw RuntimeException("Toolbar background color is invalid")
            }
        }
        return this
    }

    fun setDisplayShowTitleEnabled(showTitleAsUp: Boolean): DefaultNavigationBar {
        mActionBar?.setDisplayShowTitleEnabled(showTitleAsUp)
        return this
    }

    fun setNavigationOnClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        mToolbar?.setNavigationOnClickListener(onClickListener)
        return this
    }

    fun setLeftText(text: String?): DefaultNavigationBar {
        findViewById<TextView>(R.id.tv_left)?.text = text
        return this
    }

    fun setLeftClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.tv_left)?.setOnClickListener(onClickListener)
        return this
    }

    fun setLeftTextColor(@ColorRes colorRes: Int): DefaultNavigationBar {
        findViewById<TextView>(R.id.tv_left)?.setTextColor(
            ContextCompat.getColor(b.mContext, colorRes)
        )
        return this
    }

    fun hideLeftText(): DefaultNavigationBar {
        findViewById<View>(R.id.tv_left)?.visibility = View.GONE
        return this
    }

    fun hideTitleText(): DefaultNavigationBar {
        findViewById<View>(R.id.tv_title)?.visibility = View.GONE
        return this
    }

    fun setTitleText(title: String?): DefaultNavigationBar {
        findViewById<TextView>(R.id.tv_title)?.text = title
        return this
    }

    fun setTitleTextSize(size: Float): DefaultNavigationBar {
        findViewById<TextView>(R.id.tv_title)?.textSize = size
        return this
    }

    fun setTitleClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.tv_title)?.setOnClickListener(onClickListener)
        return this
    }

    fun setTitleTextColor(@ColorRes colorRes: Int): DefaultNavigationBar {
        findViewById<TextView>(R.id.tv_title)?.setTextColor(
            ContextCompat.getColor(b.mContext, colorRes)
        )
        return this
    }

    fun setElevation(elevation: Float) {
        if (Build.VERSION.SDK_INT >= 21 && elevation >= 0) {
            setDefaultAppBarLayoutStateListAnimator(
                findViewById<AppBarLayout>(R.id.navigation_header_container),
                elevation
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setDefaultAppBarLayoutStateListAnimator(view: View?, elevation: Float) {
        if (view == null) return
        val dur = view.resources.getInteger(R.integer.app_bar_elevation_anim_duration)
        val sla = StateListAnimator()

        sla.addState(
            intArrayOf(android.R.attr.state_enabled, R.attr.state_liftable, -R.attr.state_lifted),
            ObjectAnimator.ofFloat(view, "elevation", 0f).setDuration(dur.toLong())
        )

        sla.addState(
            intArrayOf(android.R.attr.state_enabled),
            ObjectAnimator.ofFloat(view, "elevation", elevation).setDuration(dur.toLong())
        )

        sla.addState(IntArray(0), ObjectAnimator.ofFloat(view, "elevation", 0f).setDuration(0))
        view.stateListAnimator = sla
    }

    fun hideRightView(): DefaultNavigationBar {
        findViewById<View>(R.id.iv_right)?.visibility = View.GONE
        return this
    }

    fun setRightViewClickListener(onClickListener: View.OnClickListener?): DefaultNavigationBar {
        findViewById<View>(R.id.iv_right)?.setOnClickListener(onClickListener)
        return this
    }

    fun setRightResId(rightResId: Int): DefaultNavigationBar {
        findViewById<ImageView>(R.id.iv_right)?.setImageResource(rightResId)
        return this
    }

    open class Builder(val context: Context?, parent: ViewGroup?) :
        AbsNavigationBar.Builder<Builder?>(
            context!!,
            R.layout.ui_defualt_navigation_bar,
            parent!!
        ) {
        var mLeftVisible = View.VISIBLE
        private var mDefaultNavigationBar: DefaultNavigationBar? = null
        var mTitleVisible = View.VISIBLE

        var mNavigationOnClickListener: View.OnClickListener? = null
        var mToolbarBackgroundColor =
            if (context != null) ContextCompat.getColor(context, R.color.ui_color_01a8e3) else 0
        var mShowHomeAsUp = false
        var mShowTitle = false
        var mRightViewVisible = View.VISIBLE

        var mRightResId = 0
        var mRightResHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        var mRightResWidth = ViewGroup.LayoutParams.WRAP_CONTENT

        var mToolbarBackIcon: Drawable? = null
        var mHomeAsUpIndicator: Int? = null
        var mHomeAsUpIndicatorDrawable: Drawable? = null

        fun setNavigationIcon(@DrawableRes resId: Int): Builder {
            setNavigationIcon(
                if (context != null) AppCompatResources.getDrawable(context, resId) else null
            )
            return this
        }

        fun setNavigationIcon( icon: Drawable?): Builder {
            mToolbarBackIcon = icon
            return this
        }

        fun setHomeAsUpIndicator(indicator: Drawable): Builder {
            mHomeAsUpIndicatorDrawable = indicator
            return this
        }

        fun setHomeAsUpIndicator(@DrawableRes resId: Int): Builder {
            this.mHomeAsUpIndicator = resId
            return this
        }

        fun setLeftText(text: CharSequence?, typeface: Typeface = Typeface.DEFAULT): Builder {
            setText(R.id.tv_left, text, typeface)
            return this
        }

        fun setLeftClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.tv_left, onClickListener)
            return this
        }

        fun setLeftTextColor(color: Int): Builder {
            setTextColor(R.id.tv_left, color)
            return this
        }

        fun hideLeftText(): Builder {
            mLeftVisible = View.GONE
            return this
        }

        fun setTitleText(text: CharSequence?, typeface: Typeface = Typeface.DEFAULT): Builder {
            setText(R.id.tv_title, text, typeface)
            return this
        }

        fun setTitleClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.tv_title, onClickListener)
            return this
        }

        fun setTitleTextColor(color: Int): Builder {
            setTextColor(R.id.tv_title, color)
            return this
        }

        fun hideTitleText(): Builder {
            mTitleVisible = View.GONE
            return this
        }

        fun hideRightView(): Builder {
            mRightViewVisible = View.GONE
            return this
        }

        fun showRightView(): Builder {
            mRightViewVisible = View.VISIBLE
            return this
        }

        fun setRightViewClickListener(onClickListener: View.OnClickListener?): Builder {
            setOnClickListener(R.id.iv_right, onClickListener)
            return this
        }

        fun setToolbarBackgroundColor(@ColorRes id: Int): Builder {
            mToolbarBackgroundColor = id
            return this
        }

        fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean): Builder {
            mShowHomeAsUp = showHomeAsUp
            return this
        }

        fun setDisplayShowTitleEnabled(showTitle: Boolean): Builder {
            mShowTitle = showTitle
            return this
        }

        fun setNavigationOnClickListener(onClickListener: View.OnClickListener?): Builder {
            mNavigationOnClickListener = onClickListener
            return this
        }

        fun setRightResId(rightResId: Int): Builder {
            mRightResId = rightResId
            return this
        }

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