package com.peakmain.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build.VERSION_CODES.P
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.peakmain.ui.utils.SizeUtils.screenHeight

/**
 * author peakmain
 * create：2019/7/9
 * mail:2726449200@qq.com
 * description:Special placement parameters
 */
class AlertController(val dialog: AlertDialog, val window: Window) {
    private lateinit var mViewHelper: ViewHelper

    fun setText(viewId: Int, text: CharSequence?) {
        mViewHelper.setText(viewId, text)
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        mViewHelper.setOnClickListener(viewId, listener)
    }

    fun <T : View> getView(viewId: Int): T? {
        return mViewHelper.getView(viewId)
    }

    internal class AlertParams(var mContext: Context, var themeResId: Int) {


        //click blank to cancel
        @JvmField
        var mCancelable = true

        //dialog Cancel
        @JvmField
        var mOnCancelListener: DialogInterface.OnCancelListener? = null

        //dialog Dismiss
        @JvmField
        var mOnDismissListener: DialogInterface.OnDismissListener? = null

        //Keys to monitor
        @JvmField
        var mOnKeyListener: DialogInterface.OnKeyListener? = null

        //Loading layout  view
        @JvmField
        var mView: View? = null

        //id  loading layout
        @JvmField
        var mViewLayoutResId = 0

        //save the text in the custom view
        @JvmField
        var mTextArray = SparseArray<CharSequence>()

        ///save the button click in the custom view
        @JvmField
        var mClickArray = SparseArray<View.OnClickListener>()

        @JvmField
        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT

        @JvmField
        var mGravity = Gravity.CENTER

        @JvmField
        var mAnimation = 0

        @JvmField
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        @JvmField
        var mMaxHeight: Double = 0.0


        /**
         * Bind and set parameters
         */
        fun apply(mAlert: AlertController) {
            //set parameters
            //1.set layout
            var viewHelper: ViewHelper? = null
            if (mViewLayoutResId != 0) {
                viewHelper = ViewHelper(mContext, mViewLayoutResId)
            }
            if (mView != null) {
                viewHelper = ViewHelper()
                viewHelper.contentView = mView
            }
            requireNotNull(viewHelper) { "Plase Set the layout setContentView()" }
            //Set the layout for the dialog
            mAlert.dialog.setContentView(viewHelper.contentView!!)
            //2.set text
            val textArraysize = mTextArray.size()
            for (i in 0 until textArraysize) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i))
            }
            //3.set click
            val clickSize = mClickArray.size()
            for (i in 0 until clickSize) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i))
            }
            mAlert.mViewHelper = viewHelper
            //4.Configure custom effects to pop up the default animation from the bottom of the full screen
            val window = mAlert.window
            //set gravity
            window.setGravity(mGravity)
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation)
            }
            //set width and height
            val layoutParams = window.attributes
            if(mMaxHeight>0){
                when (layoutParams.height) {
                    WindowManager.LayoutParams.MATCH_PARENT -> {
                        layoutParams.height = screenHeight.coerceAtMost(mMaxHeight.toInt());  // 如果是MATCH_PARENT，设置为屏幕高度和最大高度的较小值
                    }
                    WindowManager.LayoutParams.WRAP_CONTENT -> {
                        layoutParams.height = screenHeight.coerceAtMost(mMaxHeight.toInt()) // 可以加一个最大值限制
                    }
                    else -> {
                        // 如果不是MATCH_PARENT或WRAP_CONTENT，直接设置最大高度
                        layoutParams.height = layoutParams.height.coerceAtMost(mMaxHeight.toInt())
                    }
                }
            }else{
                layoutParams.height = mHeight
            }
            layoutParams.width = mWidth
            window.attributes = layoutParams
        }

    }

}