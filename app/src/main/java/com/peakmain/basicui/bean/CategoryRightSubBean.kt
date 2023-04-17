package com.peakmain.basicui.bean

/**
 * author ：Peakmain
 * createTime：2023/04/17
 * mail:2726449200@qq.com
 * describe：
 */
data class CategoryRightSubBean(val subTitle:String, val activityList:List<String>)

data class CategoryRightBean(var title:String="", var categoryRightBeans: ArrayList<CategoryRightSubBean>)

