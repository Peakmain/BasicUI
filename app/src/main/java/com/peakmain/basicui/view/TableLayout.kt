package com.peakmain.basicui.view

import android.content.Context
import android.util.AttributeSet
import com.peakmain.ui.tablayout.BaseTabLayout

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class TableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTabLayout<String>(context, attrs, defStyleAttr) {
    override fun setTableTitle(bean: List<String>?, position: Int): String {
        return bean!![position]
    }
}