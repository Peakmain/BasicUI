package com.peakmain.ui.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.peakmain.ui.R;
import com.peakmain.ui.recyclerview.group.GroupRecyclerBean;
import com.peakmain.ui.utils.SizeUtils;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/2/29
 * mail:2726449200@qq.com
 * describe：基本悬浮列表
 */
public abstract class BaseSuspenisonItemDecoration<T extends GroupRecyclerBean> extends RecyclerView.ItemDecoration {
    private List<T> mData;
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
    //文字距离左边的padding
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
   private boolean isCenter;
    public BaseSuspenisonItemDecoration(Context context, List<T> data) {
        this.mData = data;
        mBgColor = ContextCompat.getColor(context, android.R.color.white);
        mSectionHeight = SizeUtils.dp2px(context, 30);
        topHeight = SizeUtils.dp2px(context, 10);
        mTextSize = SizeUtils.dp2px(context, 10);
        mTextColor = ContextCompat.getColor(context, R.color.color_4A4A4A);
        mPaddingLeft = SizeUtils.dp2px(context, 10);
        mPaddingBottom = SizeUtils.dp2px(context, 5);
        mPaddingRight = SizeUtils.dp2px(context, 10);
        mPaddingTop = SizeUtils.dp2px(context, 5);
        initPaint();
        mBounds = new Rect();
    }

    public BaseSuspenisonItemDecoration(Builder builder) {
        this.mData = builder.mData;
        mBgColor = builder.mBgColor != 0 ? builder.mBgColor : ContextCompat.getColor(builder.mContext, android.R.color.white);
        mSectionHeight = builder.mSectionHeight != 0 ? builder.mSectionHeight : SizeUtils.dp2px(builder.mContext, 30);
        topHeight = builder.topHeight != 0 ? builder.topHeight : SizeUtils.dp2px(builder.mContext, 10);
        mTextSize = builder.mTextSize != 0 ? builder.mTextSize : SizeUtils.dp2px(builder.mContext, 10);
        mTextColor = builder.mTextColor != 0 ? builder.mTextColor : ContextCompat.getColor(builder.mContext, R.color.color_4A4A4A);
        mPaddingLeft = builder.mPaddingLeft != 0 ? builder.mPaddingLeft : SizeUtils.dp2px(builder.mContext, 10);
        mPaddingBottom = builder.mPaddingBottom != 0 ? builder.mPaddingBottom : SizeUtils.dp2px(builder.mContext, 5);
        mPaddingRight = builder.mPaddingRight != 0 ? builder.mPaddingRight : SizeUtils.dp2px(builder.mContext, 10);
        mPaddingTop = builder.mPaddingTop != 0 ? builder.mPaddingTop : SizeUtils.dp2px(builder.mContext, 5);
        isCenter=builder.mIsCeneter;
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
        String topText = getTopText(mData, position);
        if (!TextUtils.isEmpty(topText)) {
            Rect rect = new Rect(left,
                    child.getTop() - params.topMargin - mSectionHeight,
                    right,
                    child.getTop() - params.topMargin);
            c.drawRect(rect, mBgPaint);
            mTextPaint.getTextBounds(topText,
                    0,
                    getTopText(mData, position).length(),
                    mBounds);
            if(isCenter){
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                c.drawText(topText,
                        rect.centerX(),
                        child.getTop() - params.topMargin - mSectionHeight / 2 + mBounds.height() / 2,
                        mTextPaint);
            }else{

                c.drawText(topText,
                        child.getPaddingLeft() + mPaddingLeft,
                        child.getTop() - params.topMargin - mSectionHeight / 2 + mBounds.height() / 2,
                        mTextPaint);
            }

        }

    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = 0;
        pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
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
        Rect rect = new Rect(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mSectionHeight + mPaddingBottom / 2 + mPaddingTop / 2);
        c.drawRect(rect, mBgPaint);
        if (!TextUtils.isEmpty(section)) {
            mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
           if(isCenter){
               mTextPaint.setTextAlign(Paint.Align.CENTER);
               c.drawText(section,
                       rect.centerX(),
                       parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + mPaddingTop / 4,
                       mTextPaint);
           }else{
               c.drawText(section,
                       child.getPaddingLeft() + mPaddingLeft,
                       parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + mPaddingTop / 4,
                       mTextPaint);
           }
        } else if (pos == 0 || mData.get(pos).isHeader) {
            section = getTopText(mData, pos + 1);
            if (!TextUtils.isEmpty(section)) {
                mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
                if(isCenter){
                    c.drawText(section,
                            rect.centerX(),
                            parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + mPaddingTop / 4,
                            mTextPaint);
                }else{
                    c.drawText(section,
                            child.getPaddingLeft() + mPaddingLeft,
                            parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2) + mPaddingBottom / 4 + mPaddingTop / 4,
                            mTextPaint);
                }

            }
        }

        if (flag) {
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                if (position / (getSpanCount(parent) + 1) == 0) {
                    outRect.set(0, topHeight
                            , 0, 0);
                } else {
                    if (mData.get(position).isHeader) {
                        outRect.set(0, mSectionHeight + topHeight / 2, 0, 0);
                    }
                }
            } else {
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
    }

    /**
     * 设置置顶的文字
     */
    public abstract String getTopText(List<T> data, int position);

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 1;
    }

    public abstract static class Builder<B extends Builder, T> {
        protected Context mContext;
        private List<T> mData;
        private int mBgColor;
        private int mSectionHeight;
        private int topHeight;
        private int mTextSize;
        private int mTextColor;
        private int mPaddingLeft;
        private int mPaddingRight;
        private int mPaddingTop;
        private int mPaddingBottom;
        private boolean mIsCeneter;

        public Builder(Context context, List<T> data) {
            mContext = context;
            mData = data;
        }

        /**
         * 设置背景颜色
         *
         * @param bgColor 背景颜色
         */
        public B setBgColor(@ColorInt int bgColor) {
            this.mBgColor = bgColor;

            return (B) this;
        }

        /**
         * 设置文字到左边的距离
         *
         * @param paddingLeft paddingLeft
         */
        public B setPaddingLeft(int paddingLeft) {
            mPaddingLeft = paddingLeft;
            return (B) this;
        }

        /**
         * 设置文字到左边的距离
         *
         * @param paddingBottom paddingBottom
         */
        public B setPaddingBottom(int paddingBottom) {
            mPaddingBottom = paddingBottom;
            return (B) this;
        }

        /**
         * 设置文字到右边的距离
         *
         * @param paddingRight paddingRight
         */
        public B setPaddingRight(int paddingRight) {
            mPaddingRight = paddingRight;
            return (B) this;
        }

        /**
         * 设置文字到顶部的距离
         *
         * @param paddingTop paddingTop
         */
        public B setPaddingTop(int paddingTop) {
            mPaddingTop = paddingTop;
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
        public B setTextCenter(boolean isCenter){
            this.mIsCeneter=isCenter;
            return (B) this;
        }
        protected abstract BaseSuspenisonItemDecoration create();
    }

}
