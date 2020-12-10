package com.peakmain.basicui.launcher;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.peakmain.ui.utils.launcher.task.Task;
import com.peakmain.ui.utils.launcher.task.TaskCallBack;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class AMapTask extends Task {
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            // 一些处理
        }
    };


    @Override
    public void run() {

        mLocationClient = new AMapLocationClient(mContext);
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }
}
