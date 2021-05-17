package com.peakmain.ui.wheelview.adapter;

import java.util.List;

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe：
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {

	// items
	private List<T> items;

	/**
	 * Constructor
	 * @param items the items
	 */
	public ArrayWheelAdapter(List<T> items) {
		this.items = items;

	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int indexOf(Object o){
		return items.indexOf(o);
	}

}
