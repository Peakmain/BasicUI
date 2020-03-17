package com.peakmain.ui.adapter.bean;

import java.io.Serializable;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class AddressSelectBean<T> implements Serializable {
    private boolean isSelect;
    private T data;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
