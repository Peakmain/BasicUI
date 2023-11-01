package com.peakmain.ui.recyclerview.smooth

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * author ：Peakmain
 * createTime：2023/11/1
 * mail:2726449200@qq.com
 * describe：
 */
class EndSmoothScroller(context: Context?) : LinearSmoothScroller(context) {


    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_END
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_END
    }
}