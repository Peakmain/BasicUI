package com.peakmain.basicui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.peakmain.basicui.R;
import com.peakmain.ui.adapter.menu.BaseListMenuAdapater;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
public class ListMenuAdapter extends BaseListMenuAdapater {
    private List<String> mTitles;
    private Context mContext;
    public ListMenuAdapter(Context context, List<String> titles) {
        super(context, titles);
        this.mTitles=titles;
        this.mContext=context;
    }

    @Override
    public int getTitleLayoutId() {
        return R.layout.ui_list_data_screen_tab;
    }

    @Override
    protected void setMenuContent(View menuView, final int position) {
        TextView tv = menuView.findViewById(R.id.tv_menu_tab_content);
        tv.setText(mTitles.get(position));
        tv.setOnClickListener(v -> {
            Toast.makeText(mContext,mTitles.get(position),Toast.LENGTH_LONG).show();
            closeMenu();
        });
    }

    @Override
    protected int getMenuLayoutId() {
        return R.layout.ui_list_data_screen_menu;
    }


}
