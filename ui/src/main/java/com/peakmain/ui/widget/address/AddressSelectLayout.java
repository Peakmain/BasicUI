package com.peakmain.ui.widget.address;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peakmain.ui.R;
import com.peakmain.ui.adapter.address.AddressSelectObserver;
import com.peakmain.ui.adapter.address.BaseAddressSelectAdapter;
import com.peakmain.ui.constants.BasicUIConstants;
import com.peakmain.ui.utils.SizeUtils;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class AddressSelectLayout extends LinearLayout {
    private LinearLayout mTableLayout;
    private RecyclerView mRecyclerView;
    private BaseAddressSelectAdapter mAdapter;


    public int mCurrentType = BasicUIConstants.PROVICE_TYPE;

    public AddressSelectLayout(Context context) {
        this(context, null);
    }

    public AddressSelectLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddressSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ui_address_select, this);
        mTableLayout = findViewById(R.id.ll_table_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        for (int i = 0; i < 4; i++) {
            TextView mTextView = new TextView(context);
            mTextView.setText("请选择");
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setPadding(0, 0, SizeUtils.dp2px(getContext(), 20), 0);
            mTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            mTextView.setVisibility(INVISIBLE);
            mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null
                    , ContextCompat.getDrawable(getContext(), R.drawable.down_address_select_title));
            mTableLayout.addView(mTextView);
        }
        mTableLayout.getChildAt(0).setVisibility(VISIBLE);
        initListener();
    }

    private void initListener() {
        for (int i = 0; i < mTableLayout.getChildCount(); i++) {
            final int finalI = i;

            mTableLayout.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int currentType = getCurrentType();
                    switch (finalI) {
                        case 0:
                            mCurrentType = BasicUIConstants.PROVICE_TYPE;
                            break;
                        case 1:
                            mCurrentType = BasicUIConstants.CITY_TYPE;
                            break;
                        case 2:
                            mCurrentType = BasicUIConstants.AREA_TYPE;
                            break;
                        case 3:
                            mCurrentType = BasicUIConstants.STREET_TYPE;
                            break;
                        default:
                            break;
                    }

                    if (mChangeDataListener != null) {
                        mChangeDataListener.onChangeData();
                    }
                    TextView preTextView = (TextView) mTableLayout.getChildAt(currentType-1);
                    preTextView.setVisibility(VISIBLE);
                    preTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_4A4A4A));
                    preTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null
                            , null);
                    preTextView.setTypeface(Typeface.DEFAULT);
                    TextView currentTextView = (TextView) mTableLayout.getChildAt(mCurrentType-1);
                    currentTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null
                            , ContextCompat.getDrawable(getContext(), R.drawable.down_address_select_title));
                    currentTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            });
        }
    }

    private class AdapterAddressObserver extends AddressSelectObserver {

        @Override
        public void notifyDataChange() {
            String selectName = mAdapter.getSelectName();
            TextView currentTextView, nextTextView;
            switch (getCurrentType()) {
                case BasicUIConstants.PROVICE_TYPE:
                    setCurrentTextView(selectName, 0);
                    mCurrentType = BasicUIConstants.CITY_TYPE;

                    nextTextView = (TextView) mTableLayout.getChildAt(1);
                    nextTextView.setVisibility(VISIBLE);
                    break;
                case BasicUIConstants.CITY_TYPE:
                    setCurrentTextView(selectName, 1);
                    mCurrentType = BasicUIConstants.AREA_TYPE;
                    nextTextView = (TextView) mTableLayout.getChildAt(2);
                    nextTextView.setVisibility(VISIBLE);
                    break;
                case BasicUIConstants.AREA_TYPE:
                    setCurrentTextView(selectName, 2);
                    mCurrentType = BasicUIConstants.STREET_TYPE;
                    nextTextView = (TextView) mTableLayout.getChildAt(3);
                    nextTextView.setVisibility(VISIBLE);
                    break;
                case BasicUIConstants.STREET_TYPE:
                    currentTextView = (TextView) mTableLayout.getChildAt(3);
                    currentTextView.setTypeface(Typeface.DEFAULT);
                    currentTextView.setText(selectName);
                    currentTextView.setVisibility(VISIBLE);
                    currentTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_4A4A4A));
                    break;
                default:
                    break;
            }

            if (mChangeDataListener != null) {
                mChangeDataListener.onChangeData();
            }

        }
    }
    //设置当前的textView
    private void setCurrentTextView(String selectName, int i) {
        TextView currentTextView;
        currentTextView = (TextView) mTableLayout.getChildAt(i);
        currentTextView.setText(selectName);
        currentTextView.setVisibility(VISIBLE);
        currentTextView.setTypeface(Typeface.DEFAULT);
        currentTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.color_4A4A4A));
        currentTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null
                , null);
    }

    //告诉用户你需要重新设置数据
    public interface OnChangeDataListener {
        void onChangeData();
    }

    public OnChangeDataListener mChangeDataListener;

    public void setChangeDataListener(OnChangeDataListener changeDataListener) {
        mChangeDataListener = changeDataListener;
    }

    private AddressSelectObserver mObserver;

    public void setAdapter(BaseAddressSelectAdapter adapter, int type) {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver();
        }
        mCurrentType = type;
        this.mAdapter = adapter;
        if (mAdapter == null) {
            throw new RuntimeException("adapter is null");
        }
        mObserver = new AdapterAddressObserver();
        mAdapter.registerDataSetObserver(mObserver);
        mRecyclerView.setAdapter(mAdapter);
    }

    public int getCurrentType() {
        return mCurrentType;
    }
}
