package com.peakmain.ui.navigationbar

import android.content.Context
import android.text.TextUtils
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.peakmain.ui.R
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/26  上午 11:45
 * mail : 2726449200@qq.com
 * describe ：
 */
open class AbsNavigationBar<B : AbsNavigationBar.Builder<*>?>(val builder: B) : INavigation {
    /**
     * 返回NavigationBar的builder
     */
    private var mNavigationBarView: View? = null

    //The reduce findViewById
    private var mViews: SparseArray<WeakReference<View>>? = null
    override fun createNavigationBar() {
        mNavigationBarView = LayoutInflater.from(builder!!.mContext)
                .inflate(builder.mLayoutId, builder.mParent, false)
        val parent = builder.mParent
        //RelativeLayout
        if (parent is RelativeLayout) {
            val childView = parent.getChildAt(0)
            val params = childView.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, R.id.navigation_header_container)
        }
        if (parent is FrameLayout) {
            val childView = parent.getChildAt(0)
            val params = childView.layoutParams as FrameLayout.LayoutParams
            mNavigationBarView?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> params.topMargin = mNavigationBarView!!.getHeight() }
        }
        if (parent is LinearLayout) {
            val linearLayout = parent
            val direction = linearLayout.orientation
            if (direction == LinearLayout.HORIZONTAL) {
                //强制转成垂直布局
                linearLayout.orientation = LinearLayout.VERTICAL
            }
        }
        if (parent is ConstraintLayout) {
            val childView = parent.getChildAt(0)
            val params = childView.layoutParams as ConstraintLayout.LayoutParams
            mNavigationBarView?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> params.topMargin = mNavigationBarView!!.getHeight() }
        }
        mViews = SparseArray()
        //添加
        attachParent(mNavigationBarView, builder.mParent)
        //绑定参数
        attachNavigationParams()
    }

    /**
     * 绑定参数
     */
    override fun attachNavigationParams() {
        val navigationParameterMaps = builder!!.mNavigationParameterMaps
        for ((viewId, parameter) in navigationParameterMaps) {
            if (!TextUtils.isEmpty(parameter!!.text)) {
                val textView = findViewById<TextView>(viewId)!!
                //设置文本
                textView.text = parameter.text
                val textColorId = parameter.textColorId
                if (textColorId != 0) {
                    try {
                        textView.setTextColor(ContextCompat.getColor(builder.mContext, textColorId))
                    } catch (e: Exception) {
                        textView.setTextColor(textColorId)
                    }
                }
                textView.setOnClickListener(parameter.clickListener)
            } else {
                val view = findViewById<View>(viewId)!!
                view.setOnClickListener(parameter.clickListener)
            }
        }
    }

    /**
     * 将NavigationView添加到父布局
     *
     * @param navigationBarView
     * @param parent
     */
    override fun attachParent(navigationBarView: View?, parent: ViewGroup?) {
        parent?.addView(navigationBarView, 0)
    }

    fun <T : View?> findViewById(viewId: Int): T? {
        val weakReference = mViews!![viewId]
        var view: View? = null
        if (weakReference != null) {
            view = weakReference.get()
        }
        if (view == null) {
            view = mNavigationBarView!!.findViewById(viewId)
            if (view != null) {
                mViews!!.put(viewId, WeakReference(view))
            }
        }
        return view as T?
    }

    /**
     * Build构建类
     * 构建Navigationbar和存储参数
     */
    abstract class Builder<B : Builder<B>?> internal constructor(var mContext: Context, var mLayoutId: Int, var mParent: ViewGroup) {
        var mNavigationParameterMaps: HashMap<Int, NavigationParameter?>
        var mNavigationParameter: NavigationParameter? = null

        /**
         * 用来创建Navigationbar
         *
         * @return
         */
        abstract fun create(): AbsNavigationBar<*>?
        fun setText(viewId: Int, text: CharSequence?): B {
            getNavigationParameter(viewId)
            mNavigationParameter!!.text = text
            mNavigationParameterMaps[viewId] = mNavigationParameter
            return this as B
        }

        fun setOnClickListener(viewId: Int, onClickListener: View.OnClickListener?): B {
            getNavigationParameter(viewId)
            mNavigationParameter!!.clickListener = onClickListener
            mNavigationParameterMaps[viewId] = mNavigationParameter
            return this as B
        }

        fun setTextColor(viewId: Int, id: Int): B {
            getNavigationParameter(viewId)
            mNavigationParameter!!.textColorId = id
            mNavigationParameterMaps[viewId] = mNavigationParameter
            return this as B
        }

        private fun getNavigationParameter(viewId: Int) {
            mNavigationParameter = mNavigationParameterMaps[viewId]
            if (mNavigationParameter == null) {
                mNavigationParameter = NavigationParameter()
            }
        }

        init {
            mNavigationParameterMaps = HashMap()
        }
    }

    init {
        createNavigationBar()
    }
}