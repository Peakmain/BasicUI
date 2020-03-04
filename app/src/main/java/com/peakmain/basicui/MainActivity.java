package com.peakmain.basicui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peakmain.ui.loading.CircleLoadingView;


public class MainActivity extends AppCompatActivity {
    private CircleLoadingView mCircleLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleLoadingView = new CircleLoadingView(this);
       // mCircleLoadingView.setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mCircleLoadingView.show();
        mCircleLoadingView.setCancelable(true);

    }
}
