package com.peakmain.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.peakmain.ui.R;

/**
 * author peakmain
 * createdata：2019/7/9
 * mail:2726449200@qq.com
 * desiption:Dialog to encapsulate
 */
public class AlertDialog extends Dialog {
    private AlertController mAlert;

    public AlertDialog(@NonNull Context context) {
        super(context);
        mAlert = new AlertController(this, getWindow());
    }

    public AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    /**
     * reduce findViewById times
     */
    public <T extends View> T getView(int viewId) {

        return mAlert.getView(viewId);
    }

    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId, listener);
    }

    public static class Builder {
        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        private AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(P.mContext, P.themeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                //Click outside to cancel
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * set view
         */
        public AlertDialog.Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        public AlertDialog.Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        /**
         * save text in the custom View
         */
        public AlertDialog.Builder setText(int layoutId, CharSequence text) {
            P.mTextArray.put(layoutId, text);
            return this;
        }

        /**
         * save button click events in the custom View
         */
        public AlertDialog.Builder setOnClickListener(int layoutId, View.OnClickListener listener) {
            P.mClickArray.put(layoutId, listener);
            return this;
        }

        /**
         * set width and height
         */
        public AlertDialog.Builder setWidthAndHeight(int width, int heigth) {
            P.mWidth = width;
            P.mHeigth = heigth;
            return this;
        }

        /**
         * Settings pop up from the bottom
         */
        public AlertDialog.Builder fromButtom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimation = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public AlertDialog.Builder setFullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            P.mHeigth = ViewGroup.LayoutParams.WRAP_CONTENT;
            return this;
        }

        /**
         * set defalut animation
         */

        public AlertDialog.Builder addDefaultAnimation() {
            P.mAnimation = R.style.dialog_scale_anim;
            return this;
        }

        /**
         * set animation
         */
        public AlertDialog.Builder setAnimation(int styleAnimation) {
            P.mAnimation = styleAnimation;
            return this;
        }

        /**
         * set cancelListener
         */
        public AlertDialog.Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * set dismissListener
         */
        public AlertDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }
        /**
         * set cancelable
         */
        public AlertDialog.Builder setCancelable(boolean flag){
            P.mCancelable=flag;
            return this;
        }
        /**
         * show dialog
         */
        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}
