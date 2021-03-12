package com.peakmain.ui.recyclerview.creator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.peakmain.ui.R
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView

/**
 * author ：Peakmain
 * createTime：2020/7/22
 * mail:2726449200@qq.com
 * describe：默认加载更多view的实现
 */
class DefalutLoadViewCreator : LoadViewCreator() {
    private lateinit var mTvStatus: TextView
    private lateinit var mTvRefreshTime: TextView
    private lateinit var mIvArrow: ImageView
    override fun getLoadView(context: Context, parent: ViewGroup): View {
        val refreshView = LayoutInflater.from(context).inflate(R.layout.ui_default_refresh_view, parent, false)
        mTvStatus = refreshView.findViewById(R.id.tv_refresh_status)
        mTvRefreshTime = refreshView.findViewById(R.id.tv_refresh_time)
        mIvArrow = refreshView.findViewById(R.id.iv_arrow_downward)
        mTvStatus.setText("上拉加载更多")
        mTvRefreshTime.setVisibility(View.GONE)
        return refreshView
    }

    override fun onPull(currentDragHeight: Int, loadViewHeight: Int, currentLoadStatus: Int) {
        if (currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            if (mIvArrow.visibility == View.GONE) {
                mIvArrow.visibility = View.VISIBLE
            }
            mTvStatus.text = "上拉加载更多"
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward)
            mIvArrow.rotation = 360f
        } else if (currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mTvStatus.text = "释放立即加载"
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward)
            mIvArrow.rotation = 180f
        }
    }

    override fun onLoading() {
        mTvStatus.text = "正在加载更多...."
        mIvArrow.setImageResource(R.drawable.ic_progress_circle)
        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        animation.repeatCount = -1
        animation.duration = 1000
        mIvArrow.startAnimation(animation)
    }

    override fun onStopLoad() {
        mIvArrow.clearAnimation()
        mIvArrow.setImageResource(R.drawable.ic_arrow_downward)
        mIvArrow.rotation = 0f
        mTvStatus.text = "上拉加载更多"
    }

    override fun onFinishLoadData() {
        mTvStatus.text = "没有更多了"
        mIvArrow.visibility = View.GONE
    }
}