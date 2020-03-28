package com.peakmain.ui.compress;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/28
 * mail:2726449200@qq.com
 * describe：压缩接口
 */
public interface OnCompressListener {
    void onStart();

    void onSuccess(List<String> list);

    void onError(Throwable e);
}
