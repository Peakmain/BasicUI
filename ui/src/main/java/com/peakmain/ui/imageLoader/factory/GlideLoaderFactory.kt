package com.peakmain.ui.imageLoader.factory

import com.peakmain.ui.imageLoader.ILoader
import com.peakmain.ui.imageLoader.glide.GlideLoader

/**
 * author ：Peakmain
 * createTime：2022/7/31
 * mail:2726449200@qq.com
 * describe：
 */
class GlideLoaderFactory: AbstractLoaderFactory() {
    override fun createLoader(): ILoader {
        return GlideLoader()
    }
}