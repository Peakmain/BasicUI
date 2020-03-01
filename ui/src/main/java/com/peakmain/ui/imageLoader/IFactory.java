package com.peakmain.ui.imageLoader;

import com.peakmain.ui.imageLoader.glide.GlideLoader;

/**
 * author ：Peakmain
 * createTime：2020/2/22
 * mail:2726449200@qq.com
 * describe：创建图片工具类
 */
public interface IFactory {

     GlideLoader createGlideLoader();
}
