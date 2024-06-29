package com.peakmain.ui.widget.password;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.widget.AppCompatEditText;

import com.peakmain.ui.R;
import com.peakmain.ui.utils.SizeUtils;

/**
 * author ：Peakmain
 * createTime：2020/3/12
 * mail:2726449200@qq.com
 * describe：支付密码的EditText
 */
public class PasswordEditText extends AppCompatEditText {
    // 画笔
    private Paint mPaint;
    // 一个密码所占的宽度
    private int mPasswordItemWidth;
    // 密码的个数默认为6位数
    private int mPasswordNumber = 6;
    // 背景边框颜色
    private int mBgColor = Color.parseColor("#d1d2d6");
    // 背景边框大小
    private int mBgSize = 0;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 分割线的颜色
    private int mDivisionLineColor = mBgColor;
    // 分割线的大小
    private int mDivisionLineSize = 1;
    // 密码圆点的颜色
    private int mPasswordColor = mDivisionLineColor;
    // 密码圆点的半径大小
    private int mPasswordRadius = 4;
    int rectSpace = SizeUtils.dp2px(10); // 矩形之间的间距

    // 背景填充颜色
    private int mBgFillColor = Color.parseColor("#f1f4f6");
    // 是否显示密码
    private boolean isPasswordVisible = false;
    /**
     * 设置当前密码已完成
     */

    private PasswordCompleteListener mListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
        initPaint();
        //设置默认只能设置数字和字母
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        // 获取大小
        mDivisionLineSize = (int) ta.getDimension(R.styleable.PasswordEditText_divisionLineSize, SizeUtils.dp2px(mDivisionLineSize));
        mPasswordRadius = (int) ta.getDimension(R.styleable.PasswordEditText_passwordRadius, SizeUtils.dp2px(mPasswordRadius));
        mBgSize = (int) ta.getDimension(R.styleable.PasswordEditText_bgSize, SizeUtils.dp2px(mBgSize));
        mBgCorner = (int) ta.getDimension(R.styleable.PasswordEditText_bgCorner, 0);
        // 获取颜色
        mBgColor = ta.getColor(R.styleable.PasswordEditText_bgColor, mBgColor);
        mBgFillColor = ta.getColor(R.styleable.PasswordEditText_bgFillColor, mBgFillColor);
        mDivisionLineColor = ta.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor);
        mPasswordColor = ta.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor);
        mPasswordNumber = ta.getInt(R.styleable.PasswordEditText_passwordNumber, mPasswordNumber);
        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //一个密码的宽度
        mPasswordItemWidth = (getWidth() - 2 * mBgSize - (mPasswordNumber - 1) * rectSpace) / mPasswordNumber;
        //画背景
        drawBackground(canvas);
        // 画分割线
        // drawDivisionLine(canvas);
        //画密码
        drawPassWord(canvas);
    }

    /**
     * 绘制密码
     */
    private void drawPassWord(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPasswordColor);
        Editable text = getText();
        if (text == null) return;
        String password = text.toString().trim();
        int len = password.length();
        Rect textBounds = new Rect();
        for (int i = 0; i < len; i++) {
            int cy = getHeight() / 2;
            int cx = mBgSize + i * (mPasswordItemWidth + rectSpace) + mPasswordItemWidth / 2;
            if (isPasswordVisible) {
                String charStr = String.valueOf(password.charAt(i));
                mPaint.setTextSize(mPasswordItemWidth/2.0f);
                mPaint.getTextBounds(charStr, 0, 1, textBounds);
                float textWidth = mPaint.measureText(charStr);
                float textHeight = textBounds.height();
                canvas.drawText(charStr, cx - textWidth / 2, cy + textHeight / 2, mPaint);
            } else {
                canvas.drawCircle(cx, cy, mPasswordRadius, mPaint);
            }
        }
    }

    //分割线
    private void drawDivisionLine(Canvas canvas) {
        mPaint.setStrokeWidth(mDivisionLineSize);
        mPaint.setColor(mDivisionLineColor);
        for (int i = 0; i < mPasswordNumber - 1; i++) {
            float startX = (i + 1) * mPasswordItemWidth + mBgSize + mDivisionLineSize;
            float startY = mBgSize;
            float endY = getHeight() - mBgSize;
            canvas.drawLine(startX, startY, startX, endY, mPaint);
        }
    }

    //画背景
    // 画背景
    private void drawBackground(Canvas canvas) {
        for (int i = 0; i < mPasswordNumber; i++) {
            int left = mBgSize + i * (mPasswordItemWidth + rectSpace);
            int right = left + mPasswordItemWidth;
            RectF rectF = new RectF(left, mBgSize, right, getHeight() - mBgSize);

            // 绘制填充颜色
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mBgFillColor);
            if (mBgCorner == 0) {
                // 矩形
                canvas.drawRect(rectF, mPaint);
            } else {
                // 圆角矩形
                canvas.drawRoundRect(rectF, mBgCorner, mBgCorner, mPaint);
            }

            // 绘制边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mBgColor);
            mPaint.setStrokeWidth(mBgSize);
            if (mBgCorner == 0) {
                // 矩形
                canvas.drawRect(rectF, mPaint);
            } else {
                // 圆角矩形
                canvas.drawRoundRect(rectF, mBgCorner, mBgCorner, mPaint);
            }
        }
    }

    /**
     * 添加密码
     */
    public void addPasswordNumber(String number) {
        Editable text = getText();
        if (text == null) return;
        String passWord = text.toString().trim();
        if (passWord.length() >= mPasswordNumber) {//当前密码的长度大于当前密码数量return
            return;
        }
        passWord += number;
        setText(passWord);
        //判断是否添加密码是否添加完成
        if (mListener != null && passWord.length() >= mPasswordNumber) {
            mListener.passwordComplete(getText().toString().trim());
        }
    }

    /**
     * 删除密码
     */
    public void deletePassWord() {
        Editable text = getText();
        if (text == null) return;
        String passWord = text.toString().trim();
        if (passWord.isEmpty()) {
            return;
        }
        passWord = passWord.substring(0, passWord.length() - 1);
        setText(passWord);
    }


    public void setPasswordCompleteListener(PasswordCompleteListener listener) {
        mListener = listener;
    }
    public void setError(boolean isError) {

        if (isError) {
           setHint("密码输入有误");
           setHintTextColor(getResources().getColor(R.color.ui_color_e2263d));
        } else {
            setHint("请输入密码");
            setHintTextColor(getResources().getColor(R.color.ui_color_a6a6a6));
        }


    }

    /**
     * 密码已经全部完成
     */
    public interface PasswordCompleteListener {
        void passwordComplete(String password);
    }

    public boolean isPasswordVisible() {
        return isPasswordVisible;
    }

    public void setPasswordVisible(boolean isPasswordVisible) {
        this.isPasswordVisible = isPasswordVisible;
        invalidate();
    }
}
