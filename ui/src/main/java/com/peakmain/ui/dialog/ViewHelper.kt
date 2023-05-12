package com.peakmain.ui.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference

/**
 * author peakmain
 * create：2019/7/9
 * mail:2726449200@qq.com
 * description:Layout helper classe
 */
internal class ViewHelper() {
    var contentView: View? = null

    //The reduce findViewById
    private val mViews: SparseArray<WeakReference<View>> = SparseArray()

    constructor(context: Context?, viewLayoutResId: Int) : this() {
        contentView = LayoutInflater.from(context).inflate(viewLayoutResId, null)
    }

    fun setText(viewId: Int, text: CharSequence?) {
        val textView = getView<TextView>(viewId)
        if (textView != null) {
            textView.text = text
        }
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
    }

    fun <T : View> getView(viewId: Int): T? {
        val weakReference = mViews[viewId]
        var view: View? = null
        if (weakReference != null) {
            view = weakReference.get()
        }
        if (view == null) {
            view = contentView!!.findViewById(viewId)
            if (view != null) {
                mViews.put(viewId, WeakReference(view))
            }
        }
        return view as T?
    }

}