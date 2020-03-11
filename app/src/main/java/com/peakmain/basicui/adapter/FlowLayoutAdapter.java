package com.peakmain.basicui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.adapter.flow.BaseFlowAdapter;
import com.peakmain.ui.widget.CustomPopupWindow;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class FlowLayoutAdapter extends BaseFlowAdapter {
    private List<String> mData;
    private Context mContext;

    public FlowLayoutAdapter(Context context, List<String> data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_flow_layout, parent, false);
        final TextView tagTv = view.findViewById(R.id.tv_label);
        final ConstraintLayout llLocation = view.findViewById(R.id.ll_location);
        tagTv.setText(mData.get(position));
        tagTv.setOnClickListener(new View.OnClickListener() {

            private CustomPopupWindow mCustomPopupWindow;

            @Override
            public void onClick(View v) {
                tagTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                tagTv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_bg));
                mCustomPopupWindow = new CustomPopupWindow.PopupWindowBuilder(mContext)
                        .setView(R.layout.item_label_popwindow_view)
                        .setBgDarkAlpha(0.7f)
                        .setOnDissmissListener(() -> {
                            //取消选中
                            tagTv.setTextColor(Color.parseColor("#BE3468"));
                            tagTv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.check_bg));
                        })
                        .create();
                int[] location = new int[2];
                tagTv.getLocationOnScreen(location);
                mCustomPopupWindow.showAtLocation(llLocation, Gravity.NO_GRAVITY, (location[0] + tagTv.getWidth() / 2) - mCustomPopupWindow.getWidth() / 2, location[1] - tagTv.getMeasuredHeight() - tagTv.getPaddingTop() * 2);
                if (mCustomPopupWindow != null) {
                    mCustomPopupWindow.getView(R.id.tv_delete).setOnClickListener(v1 -> {
                        ToastUtils.showShort("删除成功");
                        mCustomPopupWindow.dissmiss();
                    });
                }
            }
        });

        return view;
    }
}
