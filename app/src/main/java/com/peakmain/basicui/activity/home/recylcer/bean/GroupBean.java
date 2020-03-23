package com.peakmain.basicui.activity.home.recylcer.bean;

import com.peakmain.ui.recyclerview.group.GroupRecyclerBean;

/**
 * author ：Peakmain
 * createTime：2020/3/22
 * mail:2726449200@qq.com
 * describe：
 */
public class GroupBean extends GroupRecyclerBean {
    private String url;
    private String time;

    public GroupBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public GroupBean(String url, String time) {
        this.url = url;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
