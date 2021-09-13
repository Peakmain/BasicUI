package com.peakmain.basicui.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionOwner
import com.gyf.immersionbar.components.ImmersionProxy
import com.peakmain.basicui.R

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
abstract class BaseFragmnet : Fragment(), ImmersionOwner {
    /**
     * ImmersionBar代理类
     */
    private val mImmersionProxy = ImmersionProxy(this)
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
        mImmersionProxy.onCreate(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
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
    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.ui_color_01a8e3)
                .fitsSystemWindows(true)
                .statusBarAlpha(0.3f) //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f) //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f) //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(false) //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(false) //导航栏图标是深色，不写默认为亮色
                .init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mImmersionProxy.onResume()
    }

    override fun onPause() {
        super.onPause()
        mImmersionProxy.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    override fun onLazyBeforeView() {}

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    override fun onLazyAfterView() {}

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    override fun onVisible() {}

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    override fun onInvisible() {}

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionProxy.onDestroy()
    }

    companion object {
        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }
}