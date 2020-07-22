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
 * describe：默认加载更多view的实现
 */
public class DefalutLoadViewCreator extends LoadViewCreator {
    private TextView mTvStatus;
    private TextView mTvRefreshTime;
    private ImageView mIvArrow;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.ui_default_refresh_view, parent, false);
        mTvStatus = refreshView.findViewById(R.id.tv_refresh_status);
        mTvRefreshTime = refreshView.findViewById(R.id.tv_refresh_time);
        mIvArrow = refreshView.findViewById(R.id.iv_arrow_downward);
        mTvStatus.setText("上拉加载更多");
        mTvRefreshTime.setVisibility(View.GONE);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus) {
        if (currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            if(mIvArrow.getVisibility()==View.GONE){
                mIvArrow.setVisibility(View.VISIBLE);
            }
            mTvStatus.setText("上拉加载更多");
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward);
            mIvArrow.setRotation(360);
        } else if (currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mTvStatus.setText("释放立即加载");
            mIvArrow.setImageResource(R.drawable.ic_arrow_downward);
            mIvArrow.setRotation(180);
        }
    }
    @Override
    public void onLoading() {
        mTvStatus.setText("正在加载更多....");
        mIvArrow.setImageResource(R.drawable.ic_progress_circle);
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mIvArrow.startAnimation(animation);
    }

    @Override
    public void onStopLoad() {
        mIvArrow.clearAnimation();
        mIvArrow.setImageResource(R.drawable.ic_arrow_downward);
        mIvArrow.setRotation(0);
        mTvStatus.setText("上拉加载更多");
    }
    @Override
    public void onFinishLoadData() {
        mTvStatus.setText("没有更多了");
        mIvArrow.setVisibility(View.GONE);
    }

}
