package com.peakmain.basicui.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.bean.CategoryRightSubBean
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MenuSubRightRecyclerAdapter(context: Context?, data: MutableList<CategoryRightSubBean>) :
    CommonRecyclerAdapter<CategoryRightSubBean>(
        context,
        data,
        R.layout.item_recycler_menu_categroy_right_sub
    ) {
    override fun convert(holder: ViewHolder, item: CategoryRightSubBean) {
        holder.setText(R.id.tv_sub_title, item.subTitle)
        val recyclerView = holder.getView<RecyclerView>(R.id.rv_content)
        (recyclerView?.layoutManager as GridLayoutManager).spanSizeLookup =
            ThreeColumnSpanSizeLookup()
        recyclerView.adapter = MenuSubCategroyRightRecyclerAdapter(mContext, item.activityList)
    }

}

class ThreeColumnSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return 1
    }
}