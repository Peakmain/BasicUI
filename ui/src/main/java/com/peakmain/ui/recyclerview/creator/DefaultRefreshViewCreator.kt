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
import com.peakmain.ui.utils.PreferencesUtil.Companion.instance

/**
 * author ：Peakmain
 * createTime：2020/7/22
 * mail:2726449200@qq.com
 * describe：
 */
class DefaultRefreshViewCreator : RefreshViewCreator() {
    private var mTvStatus: TextView? = null
    private var mTvRefreshTime: TextView? = null
    private var mIvArrow: ImageView? = null
    private var mContext: Context? = null

    /**
     * @param context 上下文
     * @param parent  RecyclerView
     * @return view
     */
    override fun getRefreshView(context: Context, parent: ViewGroup): View {
        val refreshView = LayoutInflater.from(context).inflate(R.layout.ui_default_refresh_view, parent, false)
        mTvStatus = refreshView.findViewById(R.id.tv_refresh_status)
        mTvRefreshTime = refreshView.findViewById(R.id.tv_refresh_time)
        mIvArrow = refreshView.findViewById(R.id.iv_arrow_downward)
        mContext = context
        return refreshView
    }

    /**
     * 正在下拉
     *
     * @param currentDragHeight    当前拖动的高度
     * @param refreshViewHeight    总的刷新高度
     * @param currentRefreshStatus 当前状态:
     */
    override fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int) {
        /*mTvRefreshTime.setText("刷新时间:2020年");*/
        mTvRefreshTime!!.text = instance!!.refreshTime
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mTvStatus!!.text = "下拉可以刷新"
            mIvArrow!!.setImageResource(R.drawable.ic_keyboard_arrow_down)
            mIvArrow!!.rotation = 360f
        } else if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mTvStatus!!.text = "释放立即刷新"
            mIvArrow!!.setImageResource(R.drawable.ic_keyboard_arrow_down)
            mIvArrow!!.rotation = 180f
        }
    }

    override fun onRefreshing() {
        mTvStatus!!.text = "正在刷新...."
        mIvArrow!!.setImageResource(R.drawable.ic_progress_circle)
        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        animation.repeatCount = -1
        animation.duration = 1000
        mIvArrow!!.startAnimation(animation)
        instance!!.saveRefreshTime()
    }

    override fun onStopRefresh() {
        mIvArrow!!.clearAnimation()
    }
}