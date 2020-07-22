package com.peakmain.basicui.activity.home.recylcer.refreshload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.peakmain.basicui.R;
import com.peakmain.ui.recyclerview.view.LoadRefreshRecyclerView;
import com.peakmain.ui.recyclerview.creator.LoadViewCreator;


/**
 * Created by Darren on 2017/1/3.
 * Email: 240336124@qq.com
 * Description: 默认样式的加载底部辅助类
 */

public class LoadMoreCreator extends LoadViewCreator {
    // 加载数据的ImageView
    private TextView mLoadTv;
    private View mRefreshIv;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_load_footer_view, parent, false);
        mLoadTv = (TextView) refreshView.findViewById(R.id.load_tv);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mLoadTv.setText("上拉加载更多");
        }
        if (currentRefreshStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING) {
            mLoadTv.setText("松开加载更多");
        }
    }

    @Override
    public void onLoading() {
        mLoadTv.setVisibility(View.INVISIBLE);
        mRefreshIv.setVisibility(View.VISIBLE);

        // 加载的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
        mLoadTv.setText("上拉加载更多");
        mLoadTv.setVisibility(View.VISIBLE);
        mRefreshIv.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onFinishLoadData() {

    }

}
