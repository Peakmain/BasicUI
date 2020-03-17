package com.peakmain.basicui.bean;

import android.text.TextUtils;

import com.peakmain.ui.adapter.bean.AddressSelectBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class SearchCityBean extends AddressSelectBean {
    private String id;
    private String name;
    private String pinyin;
    private double latitude;
    private double longitude;
    //城市名称拼音首字母
    private String sortLetters;
    private String hasDistrictButton;

    public SearchCityBean(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }

    /**
     *获取首字母
     */
    public String getSection() {
        if (TextUtils.isEmpty(pinyin)) {
            return "#";
        } else {
            String initials = pinyin.substring(0, 1);
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(initials);
            if (matcher.matches()) {
                //返回首字母的大写
                return initials.toUpperCase();
            } else if (TextUtils.equals(initials, "定") || TextUtils.equals(initials, "历")
                    || TextUtils.equals(initials, "热")) {
                return pinyin;
            }else{
                return "#";
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getHasDistrictButton() {
        return hasDistrictButton;
    }

    public void setHasDistrictButton(String hasDistrictButton) {
        this.hasDistrictButton = hasDistrictButton;
    }
}
