package com.peakmain.basicui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.bean.CategoryRightBean
import com.peakmain.basicui.bean.CategoryRightSubBean
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter
import com.peakmain.ui.recyclerview.adapter.ViewHolder

/**
 * author ：Peakmain
 * createTime：2020/3/9
 * mail:2726449200@qq.com
 * describe：
 */
class MenuRightRecyclerAdapter(context: Context?, data: List<CategoryRightBean>) :
    CommonRecyclerAdapter<CategoryRightBean>(
        context,
        data,
        R.layout.item_recycler_menu_categroy_right
    ) {
    override fun convert(holder: ViewHolder, item: CategoryRightBean) {
        holder.setText(R.id.tv_title, item.title)
        val recyclerView = holder.getView<RecyclerView>(R.id.rv_sub_content)
        recyclerView?.adapter = MenuSubRightRecyclerAdapter(mContext, item.categoryRightBeans)
    }

}