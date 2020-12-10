package com.peakmain.basicui.launcher;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.peakmain.basicui.R;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.utils.launcher.task.Task;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class UtilsTask extends Task {
    @Override
    public void run() {
        ToastUtils.setBgColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.setMsgColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    }
}
