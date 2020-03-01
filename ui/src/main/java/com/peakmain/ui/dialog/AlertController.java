package com.peakmain.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * author peakmain
 * createdata：2019/7/9
 * mail:2726449200@qq.com
 * desiption:Special placement parameters
 */
public class AlertController {

    private Window mWindow;
    private AlertDialog mAlertDialog;
    private ViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        this.mWindow = window;
        this.mAlertDialog = dialog;

    }

    public Window getWindow() {
        return mWindow;
    }

    public AlertDialog getDialog() {
        return mAlertDialog;
    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public void setViewHelper(ViewHelper mViewHelper) {
        this.mViewHelper = mViewHelper;
    }


    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    static class AlertParams {
        public Context mContext;
        public int themeResId;
        //click blank to cancel
        public boolean mCancelable = true;
        //dialog Cancel
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog Dismiss
        public DialogInterface.OnDismissListener mOnDismissListener;
        //Keys to monitor
        public DialogInterface.OnKeyListener mOnKeyListener;
        //Loading layout  view
        public View mView;
        //id  loading layout
        public int mViewLayoutResId;
        //save the text in the custom view
        SparseArray<CharSequence> mTextArray = new SparseArray<>();
        ///save the button click in the custom view
        SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mGravity = Gravity.CENTER;
        public int mAnimation;
        public int mHeigth = ViewGroup.LayoutParams.WRAP_CONTENT;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.themeResId = themeResId;
        }

        /**
         * Bind and set parameters
         */
        public void apply(AlertController mAlert) {
            //set parameters
            //1.set layout
            ViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new ViewHelper(mContext, mViewLayoutResId);
            }
            if (mView != null) {
                viewHelper = new ViewHelper();
                viewHelper.setContentView(mView);
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("Plase Set the layout setContentView()");
            }
            //Set the layout for the dialog
            mAlert.mAlertDialog.setContentView(viewHelper.getContentView());
            //2.set text
            int textArraysize = mTextArray.size();
            for (int i = 0; i < textArraysize; i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //3.set click
            int clickSize = mClickArray.size();
            for (int i = 0; i < clickSize; i++) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
            mAlert.setViewHelper(viewHelper);
            //4.Configure custom effects to pop up the default animation from the bottom of the full screen
            Window window = mAlert.getWindow();
            //set gravity
            window.setGravity(mGravity);
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }
            //set width and height
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.height = mHeigth;
            layoutParams.width = mWidth;
            window.setAttributes(layoutParams);
        }


    }
}
