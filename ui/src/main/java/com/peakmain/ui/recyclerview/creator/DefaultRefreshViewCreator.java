package com.peakmain.ui.recyclerview.creator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.peakmain.ui.R;
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView;

/**
 * author ：Peakmain
 * createTime：2020/7/22
 * mail:2726449200@qq.com
 * describe：
 */
public class DefaultRefreshViewCreator extends RefreshViewCreator {
    private TextView mTvStatus;
    private TextView mTvRefreshTime;
    private ImageView mIvArrow;


    /**
     * @param context 上下文
     * @param parent  RecyclerView
     * @return view
     */
    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.ui_default_refresh_view, parent, false);
        mTvStatus = refreshView.findViewById(R.id.tv_refresh_status);
        mTvRefreshTime = refreshView.findViewById(R.id.tv_refresh_time);
        mIvArrow = refreshView.findViewById(R.id.iv_arrow_downward);
        return refreshView;
    }

    /**
     * 正在下拉
     *
     * @param currentDragHeight    当前拖动的高度
     * @param refreshViewHeight    总的刷新高度
     * @param currentRefreshStatus 当前状态:
     */
    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        mTvRefreshTime.setText("刷新时间:2020年");
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mTvStatus.setText("下拉可以刷新");
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward);
            mIvArrow.setRotation(360);
        } else if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mTvStatus.setText("释放立即刷新");
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward);
            mIvArrow.setRotation(180);
        }
    }

    @Override
    public void onRefreshing() {
        mTvStatus.setText("正在刷新....");
        mIvArrow.setImageResource(R.drawable.ic_progress_circle);
        RotateAnimation animation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mIvArrow.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {

    }
}
