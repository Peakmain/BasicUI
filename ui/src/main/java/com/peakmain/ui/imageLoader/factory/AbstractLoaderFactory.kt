package com.peakmain.ui.imageLoader.factory

import com.peakmain.ui.imageLoader.ILoader

/**
 * author ：Peakmain
 * createTime：2022/7/31
 * mail:2726449200@qq.com
 * describe：
 */
abstract class AbstractLoaderFactory {
    abstract fun createLoader():ILoader
}