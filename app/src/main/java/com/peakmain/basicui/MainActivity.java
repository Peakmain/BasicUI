package com.peakmain.basicui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.peakmain.ui.widget.menu.ListMenuView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private ListMenuView mMenuView;
    private String arr[]={"类型","品牌","价格","更多"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenuView = findViewById(R.id.list_data_screen_view);
        mMenuView.setAdapter(new ListMenuAdapter(this, Arrays.asList(arr)));

    }

    public void click(View view) {

    }
}
