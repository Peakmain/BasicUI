package com.peakmain.ui.utils.network.callback;

import android.content.Context;

import java.util.Map;

/**
 * author ：Peakmain
 * createTime：2020/9/9
 * mail:2726449200@qq.com
 * describe：get,post方法默认回掉方法
 */
public interface EngineCallBack {
    //错误
    void onError(Exception e);

    //成功    data{"",""} 成功 失败 data ""
    void onSuccess(String result);



    //默认回调接口
    EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
