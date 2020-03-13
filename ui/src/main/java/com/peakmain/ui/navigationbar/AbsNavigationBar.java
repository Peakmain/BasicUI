package com.peakmain.ui.navigationbar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peakmain.ui.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Peakmain
 * version ：1.0
 * createTime ：2018/11/26  上午 11:45
 * mail : 2726449200@qq.com
 * describe ：
 */
public class AbsNavigationBar<B extends AbsNavigationBar.Builder> implements INavigation {
    private B mBuilder;
    private View mNavigationBarView;
    //The reduce findViewById
    private SparseArray<WeakReference<View>> mViews;

    AbsNavigationBar(B builder) {
        mBuilder = builder;
        createNavigationBar();
    }

    @Override
    public void createNavigationBar() {
        mNavigationBarView = LayoutInflater.from(mBuilder.mContext)
                .inflate(mBuilder.mLayoutId, mBuilder.mParent, false);
        ViewGroup parent = mBuilder.mParent;
        //RelativeLayout
        if (parent instanceof RelativeLayout) {
            View childView = parent.getChildAt(0);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) childView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.navigation_header_container);
        }
        if (parent instanceof FrameLayout) {
            View childView = parent.getChildAt(0);
            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) childView.getLayoutParams();
            mNavigationBarView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    params.topMargin = mNavigationBarView.getHeight();
                }
            });
        }
        if (parent instanceof LinearLayout) {
            final LinearLayout linearLayout = (LinearLayout) parent;
            int direction = linearLayout.getOrientation();

            if (direction == LinearLayout.HORIZONTAL) {
                //强制转成垂直布局
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }
        if(parent instanceof ConstraintLayout){
            View childView = parent.getChildAt(0);
            final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) childView.getLayoutParams();
            mNavigationBarView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    params.topMargin = mNavigationBarView.getHeight();
                }
            });
        }
        mViews = new SparseArray<>();
        //添加
        attachParent(mNavigationBarView, mBuilder.mParent);
        //绑定参数
        attachNavigationParams();
    }

    /**
     * 绑定参数
     */
    @Override
    public void attachNavigationParams() {
        HashMap<Integer, NavigationParameter> navigationParameterMaps = mBuilder.mNavigationParameterMaps;
        for (Map.Entry<Integer, NavigationParameter> parameterEntry : navigationParameterMaps.entrySet()) {
            Integer viewId = parameterEntry.getKey();
            NavigationParameter parameter = parameterEntry.getValue();
            if (!TextUtils.isEmpty(parameter.getText())) {
                TextView textView = findViewById(viewId);
                //设置文本
                textView.setText(parameter.getText());
                int textColorId = parameter.getTextColorId();

                if (textColorId != 0) {
                    try {
                        textView.setTextColor(ContextCompat.getColor(getBuilder().mContext, textColorId));
                    } catch (Exception e) {
                        textView.setTextColor(textColorId);
                    }
                }
                textView.setOnClickListener(parameter.getClickListener());
            } else {
                View view = findViewById(viewId);
                view.setOnClickListener(parameter.getClickListener());
            }

        }
    }

    /**
     * 将NavigationView添加到父布局
     *
     * @param navigationBarView
     * @param parent
     */
    @Override
    public void attachParent(View navigationBarView, ViewGroup parent) {
        if (parent != null) {
            parent.addView(navigationBarView, 0);
        }
    }

    /**
     * 返回NavigationBar的builder
     */
    public B getBuilder() {
        return mBuilder;
    }

    public <T extends View> T findViewById(int viewId) {
        WeakReference<View> weakReference = mViews.get(viewId);
        View view = null;
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view == null) {
            view = mNavigationBarView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }


    /**
     * Build构建类
     * 构建Navigationbar和存储参数
     */
    public abstract static class Builder<B extends Builder> {
        Context mContext;
        int mLayoutId;
        ViewGroup mParent;
        HashMap<Integer, NavigationParameter> mNavigationParameterMaps;
        NavigationParameter mNavigationParameter;

        Builder(Context context, int layoutId, ViewGroup parent) {
            mContext = context;
            mLayoutId = layoutId;
            mParent = parent;
            mNavigationParameterMaps = new HashMap<>();

        }

        /**
         * 用来创建Navigationbar
         *
         * @return
         */
        public abstract AbsNavigationBar create();

        B setText(int viewId, CharSequence text) {
            getNavigationParameter(viewId);
            mNavigationParameter.setText(text);
            mNavigationParameterMaps.put(viewId, mNavigationParameter);
            return (B) this;
        }


        B setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            getNavigationParameter(viewId);
            mNavigationParameter.setClickListener(onClickListener);
            mNavigationParameterMaps.put(viewId, mNavigationParameter);
            return (B) this;
        }

        B setTextColor(int viewId, int id) {
            getNavigationParameter(viewId);
            mNavigationParameter.setTextColorId(id);
            mNavigationParameterMaps.put(viewId, mNavigationParameter);
            return (B) this;
        }

        private void getNavigationParameter(int viewId) {
            mNavigationParameter = mNavigationParameterMaps.get(viewId);
            if (mNavigationParameter == null) {
                mNavigationParameter = new NavigationParameter();
            }
        }

    }
}
