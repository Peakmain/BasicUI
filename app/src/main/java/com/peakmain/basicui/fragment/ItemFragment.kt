package com.peakmain.basicui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.peakmain.basicui.R

/**
 * author ：Peakmain
 * createTime：2020/3/11
 * mail:2726449200@qq.com
 * describe：
 */
class ItemFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item, container, false)
        val tv = view.findViewById<TextView>(R.id.text_view1)
        val bundle = arguments
        tv.text = bundle!!.getString("title")
        return view
    }

    companion object {
        fun newInstance(item: String?): ItemFragment {
            val itemFragment = ItemFragment()
            val bundle = Bundle()
            bundle.putString("title", item)
            itemFragment.arguments = bundle
            return itemFragment
        }
    }
}