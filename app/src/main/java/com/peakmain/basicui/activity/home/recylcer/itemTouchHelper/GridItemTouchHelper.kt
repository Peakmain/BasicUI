package com.peakmain.basicui.activity.home.recylcer.itemTouchHelper

import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import com.peakmain.ui.recyclerview.adapter.ViewHolder
import com.peakmain.ui.recyclerview.itemTouch.BaseItemTouchHelper

/**
 * author ：Peakmain
 * createTime：2020/3/25
 * mail:2726449200@qq.com
 * describe：
 */
class GridItemTouchHelper(adapter: RecyclerView.Adapter<ViewHolder>?, data: List<GroupBean?>?) : BaseItemTouchHelper<GroupBean?>(adapter, data)