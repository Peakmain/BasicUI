package com.peakmain.basicui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peakmain.basicui.R;

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
public class ItemFragment extends Fragment {
    public static ItemFragment newInstance(String item) {
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", item);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item,container,false);

        TextView tv = view.findViewById(R.id.text_view1);
        Bundle bundle = getArguments();
        tv.setText(bundle.getString("title"));
        return view;
    }
}