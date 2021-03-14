package com.peakmain.ui.utils.log;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.peakmain.ui.R;
import com.peakmain.ui.utils.SizeUtils;
import com.peakmain.ui.widget.CircleImageView;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class ViewPrinterProvider {
    private final FrameLayout mRootView;
    private final RecyclerView mRecyclerView;
    public static final String FLOATING_VIEW_TAG = "FLOATING_VIEW_TAG";
    public static final String LOG_VIEW_TAG = "LOG_VIEW_TAG";
    private View mFloatingView;
    private boolean isOpen;
    private View mLogView;

    public ViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        this.mRootView = rootView;
        this.mRecyclerView = recyclerView;
    }

    public void showFloatingView() {
        if (mRootView.findViewWithTag(FLOATING_VIEW_TAG) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = createFloatingView();
        floatingView.setTag(FLOATING_VIEW_TAG);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        params.bottomMargin = SizeUtils.dp2px(100);
        mRootView.addView(createFloatingView(), params);
    }

    private View createFloatingView() {
        if (mFloatingView != null) {
            return mFloatingView;
        }
        CircleImageView circleImageView = new CircleImageView(mRootView.getContext());
        circleImageView.setImageResource(R.drawable.ic_log_flod_description_24);
        circleImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    showLoginView();
                }
            }
        });
        return mFloatingView = circleImageView;
    }

    private void showLoginView() {
        if (mRootView.findViewWithTag(LOG_VIEW_TAG) != null) {
            return;
        }
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(160));
        params.gravity = Gravity.BOTTOM;
        View logView = createLogView();
        logView.setTag(LOG_VIEW_TAG);
        mRootView.addView(createLogView(), params);
        isOpen = true;
    }
    /**
     * 关闭LogView
     */
    public void closeLogView() {
        isOpen = false;
        mRootView.removeView(createLogView());
    }
    private View createLogView() {
        if(mLogView!=null){
            return mLogView;
        }
        FrameLayout logView = new FrameLayout(mRootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(mRecyclerView);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        CircleImageView circleImageView = new CircleImageView(mRootView.getContext());
        circleImageView.setImageResource(R.drawable.ic_et_close);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogView();
            }
        });
        logView.addView(circleImageView,params);
        return mLogView=logView;
    }
}
