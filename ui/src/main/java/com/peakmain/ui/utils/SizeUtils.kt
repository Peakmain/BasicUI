package com.peakmain.ui.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.peakmain.ui.constants.BasicUIUtils

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：尺寸相关工具类
 */
object SizeUtils {


    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        val scale = BasicUIUtils.application?.resources?.displayMetrics!!.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    fun px2dp(pxValue: Float): Int {
        val scale = BasicUIUtils.application?.resources?.displayMetrics!!.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val fontScale = BasicUIUtils.application?.resources?.displayMetrics!!.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = BasicUIUtils.application?.resources!!.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 各种单位转换
     *
     * 该方法存在于TypedValue
     *
     * @param unit    单位
     * @param value   值
     * @param metrics DisplayMetrics
     * @return 转换结果
     */
    fun applyDimension(unit: Int, value: Float, metrics: DisplayMetrics): Float {
        when (unit) {
            TypedValue.COMPLEX_UNIT_PX -> return value
            TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
            TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
            TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
            TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
            TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
            else -> {
            }
        }
        return 0f
    }

    /**
     * 在onCreate中获取视图的尺寸
     *
     * 需回调onGetSizeListener接口，在onGetSize中获取view宽高
     *
     * 用法示例如下所示
     * <pre>
     * SizeUtils.forceGetViewSize(view, new SizeUtils.onGetSizeListener() {
     * Override
     * public void onGetSize(final View view) {
     * view.getWidth();
     * }
     * });
    </pre> *
     *
     * @param view     视图
     * @param listener 监听器
     */
    fun forceGetViewSize(
        view: View,
        block: ((View) -> Unit)? = null
    ) {
        view.post { block?.invoke(view) }
    }

    /**
     * 测量视图尺寸
     *
     * @param view 视图
     * @return arr[0]: 视图宽度, arr[1]: 视图高度
     */
    fun measureView(view: View): IntArray {
        var lp = view.layoutParams
        if (lp == null) {
            lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width)
        val lpHeight = lp.height
        val heightSpec: Int = if (lpHeight > 0) {
            View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY)
        } else {
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        }
        view.measure(widthSpec, heightSpec)
        return intArrayOf(view.measuredWidth, view.measuredHeight)
    }

    /**
     * 获取测量视图宽度
     *
     * @param view 视图
     * @return 视图宽度
     */
    fun getMeasuredWidth(view: View): Int {
        return measureView(view)[0]
    }

    /**
     * 获取测量视图高度
     *
     * @param view 视图
     * @return 视图高度
     */
    fun getMeasuredHeight(view: View): Int {
        return measureView(view)[1]
    }

    /**
     * 获取屏幕的宽度（单位：px）
     */
    @JvmStatic
    val screenWidth: Int
        get() = BasicUIUtils.application?.resources?.displayMetrics!!.widthPixels

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高
     */
    @JvmStatic
    val screenHeight: Int
        get() = BasicUIUtils.application?.resources?.displayMetrics!!.heightPixels

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        //获取status_bar_height资源的ID
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            context.resources.getDimensionPixelSize(resourceId)
        } else dp2px(25f)
    }

    @JvmStatic
    fun getNavBarHeight(): Int {
        val res: Resources? = BasicUIUtils.application?.resources
        val resourceId: Int =
            res?.getIdentifier("navigation_bar_height", "dimen", "android") ?: 0
        return if (resourceId != 0) {
            res?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    @JvmStatic
    fun getStatusBarHeight(): Int {
        val resources: Resources? = BasicUIUtils.application?.resources
        val resourceId = resources?.getIdentifier("status_bar_height", "dimen", "android") ?: 0
        return resources?.getDimensionPixelSize(resourceId) ?: 0
    }
}
