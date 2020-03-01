package com.peakmain.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.peakmain.ui.R;


/**
 * author ：Peakmain
 * createTime：2020/3/1
 * mail:2726449200@qq.com
 * describe：
 */
public class AutoDeleteEditText extends LinearLayout implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener {
    /**
     * 字体大小
     */
    private int mTextSize = 0;
    /**
     * 字体颜色
     */
    private int mTextColor = 0;
    /**
     * 输入文字以前字体颜色
     */
    private int mHintColor = 0;
    /**
     * 输入内容是否居上
     */
    private boolean isTop = false;
    /**
     * 内容居顶部距离
     */
    private float mPaddingTop = 0;
    /**
     * 提示语
     */
    private String mHint;
    //是否单行展示
    private boolean isSingle;
    /**
     * 输入类型
     */

    private int mInputType;
    /**
     * 输入长度
     */
    private int mMaxLength;

    private EditText mEditText;
    private ImageView mIvDelete;
    /**
     * 是否有焦点
     */

    private boolean hasFocus;
    /**
     * 删除图片的颜色
     */
    private ColorStateList mDrawableTintList = null;
    private Drawable mDrawable;

    public AutoDeleteEditText(Context context) {
        this(context, null);
    }

    public AutoDeleteEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoDeleteEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 解析自定义属性
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ui_auto_delete_edittext, this);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoDeleteEditText);
        mTextSize = (int) ta.getDimension(R.styleable.AutoDeleteEditText_adet_text_size, 0);
        mTextColor = ta.getColor(R.styleable.AutoDeleteEditText_adet_text_color, 0);
        mHintColor = ta.getColor(R.styleable.AutoDeleteEditText_adet_hint_color, 0);
        isTop = ta.getBoolean(R.styleable.AutoDeleteEditText_adet_isTop, false);
        mPaddingTop = ta.getDimension(R.styleable.AutoDeleteEditText_adet_padding_top, 0);
        mHint = ta.getString(R.styleable.AutoDeleteEditText_adet_hint);
        isSingle = ta.getBoolean(R.styleable.AutoDeleteEditText_adet_isSingle, false);
        mInputType = ta.getInt(R.styleable.AutoDeleteEditText_android_inputType, EditorInfo.TYPE_NULL);
        mMaxLength = ta.getInt(R.styleable.AutoDeleteEditText_adet_max_length, 0);
        if (ta.hasValue(R.styleable.AutoDeleteEditText_adet_tint_color)) {
            mDrawableTintList = ta.getColorStateList(R.styleable.AutoDeleteEditText_adet_tint_color);
        }
        if (ta.hasValue(R.styleable.AutoDeleteEditText_adet_delete_src)) {
            mDrawable = ta.getDrawable(R.styleable.AutoDeleteEditText_adet_delete_src);
        }
        ta.recycle();

        mEditText = findViewById(R.id.et_content);
        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(this);
        mIvDelete = findViewById(R.id.iv_delete);
        mIvDelete.setOnClickListener(this);
        setUp();
    }

    /**
     * 设置属性
     */
    private void setUp() {
        if (mEditText == null) {
            return;
        }
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mEditText.setTextColor(mTextColor);
        mEditText.setHintTextColor(mHintColor);
        if (isTop) {
            mEditText.setGravity(Gravity.TOP);
            mEditText.setPadding(0, (int) mPaddingTop, 0, 0);
        }
        if (!TextUtils.isEmpty(mHint)) {
            mEditText.setHint(mHint);
        }
        mEditText.setSingleLine(isSingle);
        if (mMaxLength > 0) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        }
        if (mInputType != 0) {
            mEditText.setInputType(mInputType);
        }
        if (mDrawableTintList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIvDelete.setImageTintList(mDrawableTintList);
            }
        }
        if (mDrawable != null) {
            mIvDelete.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (hasFocus) {
            if (s.toString().length() > 0) {
                mIvDelete.setVisibility(VISIBLE);
            } else {
                mIvDelete.setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        //如果获取焦点并且有内容则显示删除按钮
        if (hasFocus && !TextUtils.isEmpty(getText())) {
            mIvDelete.setVisibility(VISIBLE);
        } else {
            mIvDelete.setVisibility(INVISIBLE);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_delete) {
            post(new Runnable() {
                @Override
                public void run() {
                    mEditText.getText().clear();
                }
            });
        }
    }

    /**
     * 返回输入内容
     */
    public String getText() {
        return mEditText != null ? mEditText.getText().toString() : "";
    }

}
