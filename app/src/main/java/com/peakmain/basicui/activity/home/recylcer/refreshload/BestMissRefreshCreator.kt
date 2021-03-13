package com.peakmain.basicui.activity.home.recylcer.refreshload

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.peakmain.basicui.R
import com.peakmain.ui.recyclerview.creator.RefreshViewCreator
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/21 0021 下午 3:31
 * mail : 2726449200@qq.com
 * describe ：默认样式的刷新头部辅助类,这里使用的是百思不得姐
 */
class BestMissRefreshCreator : RefreshViewCreator() {
    // 加载数据的ImageView
    private var mRefreshIv: ImageView? = null
    override fun getRefreshView(context: Context, parent: ViewGroup): View? {
        val refreshView = LayoutInflater.from(context).inflate(R.layout.layout__bestmiss_refresh_header_view, parent, false)
        mRefreshIv = refreshView.findViewById(R.id.img_progress)
        return refreshView
    }

    override fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int) {
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mRefreshIv!!.setImageResource(R.drawable.list_view_pull)
        }
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mRefreshIv!!.setImageResource(R.drawable.list_view_release)
        }
    }

    override fun onRefreshing() {
        mRefreshIv!!.setImageResource(R.drawable.load_more_anim)
        (mRefreshIv!!.background as AnimationDrawable).start()
    }

    override fun onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv!!.rotation = 0f
        (mRefreshIv!!.background as AnimationDrawable).stop()
        mRefreshIv!!.clearAnimation()
    }
}