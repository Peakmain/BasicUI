package com.peakmain.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2021/7/31
 * mail:2726449200@qq.com
 * describe：
 */
class CrumbView : HorizontalScrollView {
    private val ARROW_LIGHT_COLOR = ContextCompat.getColor(context, R.color.ui_color_01a8e3)
    private val ARROW_DARK_COLOR = ContextCompat.getColor(context, R.color.ui_color_7F7F7F)
    private lateinit var mContainer: LinearLayout
    private var rootView: LinearLayout
    private var mFragmentManager: FragmentManager? = null

    constructor(context: Context) : super(context)
    constructor(context: Context,attrs: AttributeSet?) : super(context,attrs){
        initAttrs(context, attrs)
    }

    constructor(context: Context,attrs: AttributeSet?,defStyle: Int) : super(context,attrs,defStyle){
        initAttrs(context, attrs)
    }

    init {

        rootView = LinearLayout(context)
        rootView.orientation = LinearLayout.HORIZONTAL
        rootView.gravity = Gravity.CENTER_VERTICAL
        addView(rootView)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CrumbView)
        ta.getColor(R.styleable.CrumbView_light_color, ARROW_LIGHT_COLOR)
        ta.getColor(R.styleable.CrumbView_dark_color, ARROW_DARK_COLOR)
        ta.recycle()
    }

    fun setActivity(activity: FragmentActivity) {
        setActivity(activity, "")
    }

    fun setActivity(activity: FragmentActivity, label: String) {
        mFragmentManager = activity.supportFragmentManager
        mFragmentManager?.addOnBackStackChangedListener {
            updateCrumbs()
        }
        var useLab = false
        if (label.isNotEmpty()) {
            useLab = true
            setLab(activity, label)
        }
        initView(activity, useLab)
        updateCrumbs()
    }

    private fun initView(context: Context, useLab: Boolean) {
        mContainer = LinearLayout(context)
        mContainer.orientation = LinearLayout.HORIZONTAL
        var left = 0
        if (!useLab) {
            left = context.resources.getDimensionPixelOffset(R.dimen.dimen_12)
        }
        mContainer.setPadding(
            left,
            0,
            context.resources.getDimensionPixelOffset(R.dimen.dimen_12),
            0
        )
        mContainer.gravity = Gravity.CENTER_VERTICAL
        rootView.addView(mContainer)
    }

    private fun setLab(activity: FragmentActivity, label: String) {
        val itemView: View = View.inflate(this.context, R.layout.ui_crumb_item_header, null)
        val tvName: TextView = itemView.findViewById(R.id.ui_tv_name)
        tvName.apply {
            text = label
            setTextColor(ARROW_DARK_COLOR)
        }
        itemView.setOnClickListener {
            activity.finish()
        }
        rootView.addView(itemView)
    }

    private fun updateCrumbs() {
        val numFrags = mFragmentManager!!.backStackEntryCount
        var numCrumbs = mContainer.childCount
        for (i in 0..numFrags) {
            val backStackEntryAt = mFragmentManager?.getBackStackEntryAt(i)
            var itemView: View
            //移除tag!=backStackEntryAt
            if (i < numCrumbs) {
                itemView = mContainer.getChildAt(i)
                val tag = itemView.tag
                if (tag != backStackEntryAt) {
                    for (j in 0..numCrumbs) {
                        mContainer.removeViewAt(i)
                    }
                    numCrumbs = i
                }
            }
            //新增一个item
            if (i >= numCrumbs) {
                itemView =
                    View.inflate(context, R.layout.ui_crumb_item_layout, null)
                val tvName: TextView = itemView.findViewById(R.id.ui_tv_name)
                tvName.text = backStackEntryAt?.breadCrumbTitle
                itemView.tag = backStackEntryAt
                itemView.setOnClickListener {
                    val bse: FragmentManager.BackStackEntry
                    if (it.tag is FragmentManager.BackStackEntry) {
                        bse = it.tag as FragmentManager.BackStackEntry
                        mFragmentManager?.popBackStack(bse.id, 0)
                    } else {
                        val count = mFragmentManager!!.backStackEntryCount
                        if (count > 0) {
                            bse = mFragmentManager!!.getBackStackEntryAt(0)
                            mFragmentManager!!.popBackStack(bse.id, 0)
                        }
                    }
                }
                this.mContainer.addView(itemView)
            }

        }
        numCrumbs = mContainer.childCount
        while (numCrumbs > numFrags) {
            numCrumbs--
            mContainer.removeViewAt(numCrumbs - 1)
        }
        for (i in 0..numCrumbs) {
            val child = mContainer.getChildAt(i)
            highLightIndex(child, i >= numCrumbs - 1)
        }
        post {
            fullScroll(View.FOCUS_RIGHT)
        }
    }

    private fun highLightIndex(view: View, highLight: Boolean) {
        val text = view.findViewById<TextView>(R.id.ui_tv_name)
        val image = view.findViewById<ImageView>(R.id.ui_arrow_right)
        if (highLight) {
            text.setTextColor(this.ARROW_LIGHT_COLOR)
            image.visibility = View.GONE
        } else {
            text.setTextColor(this.ARROW_DARK_COLOR)
            image.visibility = View.VISIBLE
        }
    }
}