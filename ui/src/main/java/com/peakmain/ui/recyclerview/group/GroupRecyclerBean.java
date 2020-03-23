package com.peakmain.ui.recyclerview.group;

import java.io.Serializable;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
public class GroupRecyclerBean<T> implements Serializable {
    public boolean isHeader;
    public String header;

    public GroupRecyclerBean() {

    }

    public GroupRecyclerBean(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }


}
