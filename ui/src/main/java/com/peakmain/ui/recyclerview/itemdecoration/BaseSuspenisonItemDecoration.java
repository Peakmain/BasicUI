package com.peakmain.ui.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.peakmain.ui.R;
import com.peakmain.ui.utils.SizeUtils;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：基本悬浮列表
 */
public abstract class BaseSuspenisonItemDecoration<T> extends RecyclerView.ItemDecoration {
    private List mData;
    private Paint mBgPaint;
    private TextPaint mTextPaint;
    private Rect mBounds;
    //置顶距离文字的高度 默认是30
    private int mSectionHeight;
    private int mBgColor;
    private int mTextColor;
    private int mTextSize;
    //两个置顶模块之间的距离，默认是10
    private int topHeight;

    public BaseSuspenisonItemDecoration(Builder builder) {
        this.mData = builder.mData;

        mBgColor = builder.mBgColor != 0 ? builder.mBgColor : ContextCompat.getColor(builder.mContext, android.R.color.white);
        mSectionHeight = builder.mSectionHeight != 0 ? builder.mSectionHeight : SizeUtils.dp2px(builder.mContext,30);
        topHeight = builder.topHeight != 0 ? builder.topHeight : SizeUtils.dp2px(builder.mContext,10);
        mTextSize = builder.mTextSize != 0 ? builder.mTextSize : SizeUtils.dp2px(builder.mContext,10);
        mTextColor = builder.mTextColor != 0 ? builder.mTextColor : ContextCompat.getColor(builder.mContext, R.color.color_4A4A4A);
        initPaint();

        mBounds = new Rect();
    }

    private void initPaint() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mBgColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }


    public void setData(List<T> data) {
        this.mData = data;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position);
                } else {
                    if (null != getTopText(mData, position)
                            && !getTopText(mData, position).equals(getTopText(mData, position - 1))) {
                        drawSection(c, left, right, child, params, position);
                    }
                }
            }
        }
    }


    private void drawSection(Canvas c, int left, int right, View child,
                             RecyclerView.LayoutParams params, int position) {
        c.drawRect(left,
                child.getTop() - params.topMargin - mSectionHeight,
                right,
                child.getTop() - params.topMargin, mBgPaint);
        mTextPaint.getTextBounds(getTopText(mData, position),
                0,
                getTopText(mData, position).length(),
                mBounds);
        c.drawText(getTopText(mData, position),
                child.getPaddingLeft(),
                child.getTop() - params.topMargin - mSectionHeight / 2 + mBounds.height() / 2,
                mTextPaint);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (pos < 0) {
            return;
        }
        if (mData == null || mData.isEmpty()) {
            return;
        }
        String section = getTopText(mData, pos);
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;

        boolean flag = false;
        if ((pos + 1) < mData.size()) {
            if (null != section && !section.equals(getTopText(mData, pos + 1))) {
                if (child.getHeight() + child.getTop() < mSectionHeight) {
                    c.save();
                    flag = true;
                    c.translate(0, child.getHeight() + child.getTop() - mSectionHeight);
                }
            }
        }
        c.drawRect(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mSectionHeight, mBgPaint);
        mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
        c.drawText(section,
                child.getPaddingLeft(),
                parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2),
                mTextPaint);
        if (flag) {
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
            if (position == 0) {
                outRect.set(0, mSectionHeight
                        , 0, 0);
            } else {
                if (null != getTopText(mData, position)
                        && !getTopText(mData, position).equals(getTopText(mData, position - 1))) {
                    outRect.set(0, mSectionHeight + topHeight, 0, 0);
                }
            }
        }
    }

    /**
     * 设置置顶的文字
     */
    public abstract String getTopText(List<T> data, int position);

    public abstract static class Builder<B extends Builder, T> {
        protected Context mContext;
        protected List<T> mData;
        private int mBgColor;
        private int mSectionHeight;
        private int topHeight;
        private int mTextSize;
        private int mTextColor;

        public Builder(Context context, List<T> data) {
            mContext = context;
            mData = data;
        }

        public B setBgColor(int bgColor) {
            this.mBgColor = bgColor;

            return (B) this;
        }

        /**
         * 置顶距离文字的高度 默认是30
         */
        public B setSectionHeight(int sectionHeight) {
            this.mSectionHeight = sectionHeight;
            return (B) this;
        }

        /**
         * 两个置顶模块之间的距离，默认是10
         *
         * @param topHeight topHeight
         */
        public B setTopHeight(int topHeight) {
            this.topHeight = topHeight;
            return (B) this;
        }

        /**
         * 设置文字的大小
         *
         * @param textSize 文字大小
         */
        public B setTextSize(int textSize) {
            this.mTextSize = textSize;
            return (B) this;
        }

        /**
         * 设置文字的颜色
         *
         * @param textColor 文字的颜色
         */
        public B setTextColor(int textColor) {
            mTextColor = textColor;

            return (B) this;
        }

        protected abstract BaseSuspenisonItemDecoration create();
    }

}
