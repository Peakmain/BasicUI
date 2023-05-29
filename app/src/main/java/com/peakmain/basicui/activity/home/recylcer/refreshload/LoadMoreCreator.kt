package com.peakmain.basicui.activity.home.recylcer.refreshload

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.creator.LoadViewCreator
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView

/**
 * Created by Darren on 2017/1/3.
 * Email: 240336124@qq.com
 * Description: 默认样式的加载底部辅助类
 */
class LoadMoreCreator : LoadViewCreator() {
    // 加载数据的ImageView
    private var mLoadTv: TextView? = null
    private var mRefreshIv: View? = null
    override fun getLoadView(context: Context, parent: ViewGroup): View? {
        val refreshView = LayoutInflater.from(context).inflate(R.layout.layout_load_footer_view, parent, false)
        mLoadTv = refreshView.findViewById<View>(R.id.load_tv) as TextView
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv)
        return refreshView
    }

    override fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int) {
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mLoadTv!!.text = "上拉加载更多"
        }
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mLoadTv!!.text = "松开加载更多"
        }
    }

    override fun onLoading() {
        mLoadTv!!.visibility = View.INVISIBLE
        mRefreshIv!!.visibility = View.VISIBLE

        // 加载的时候不断旋转
        val animation = RotateAnimation(0f, 720f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.repeatCount = -1
        animation.duration = 1000
        mRefreshIv!!.startAnimation(animation)
    }

    override fun onStopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv!!.rotation = 0f
        mRefreshIv!!.clearAnimation()
        mLoadTv!!.text = "上拉加载更多"
        mLoadTv!!.visibility = View.VISIBLE
        mRefreshIv!!.visibility = View.INVISIBLE
    }

    override fun onFinishLoadData() {
        mLoadTv!!.text = "无更多数据"
    }
}