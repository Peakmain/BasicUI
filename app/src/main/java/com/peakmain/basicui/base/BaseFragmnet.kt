package com.peakmain.basicui.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peakmain.basicui.R

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseFragmnet : Fragment() {

    private var mRootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val ft = fragmentManager!!.beginTransaction()
            val isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflaterView(inflater, container)
        initView(mRootView)
        initData()
        return mRootView
    }

    private fun inflaterView(inflater: LayoutInflater, container: ViewGroup?) {
        if (mRootView == null) {
            mRootView = inflater.inflate(layoutId, container, false)
        }
    }
    fun getRootView():View{
        return mRootView!!
    }

    protected abstract val layoutId: Int
    protected abstract fun initView(view: View?)
    protected abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }
}