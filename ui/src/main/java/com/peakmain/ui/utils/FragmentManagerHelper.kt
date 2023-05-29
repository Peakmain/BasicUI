package com.peakmain.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * author ：Peakmain
 * createTime：2021/3/26
 * mail:2726449200@qq.com
 * describe：Fragment的帮助类
 */
class FragmentManagerHelper constructor(
    private var mFragmentManager: FragmentManager,
    private var mContainerViewId: Int
) {
    /**
     * 添加fragment
     */
    fun addFragment(fragment: Fragment) {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.add(mContainerViewId, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * 切换fragment
     */
    fun switchFragment(fragment: Fragment) {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        val fragments = mFragmentManager.fragments
        fragments.forEach {
            fragmentTransaction.hide(it)
        }
        if (!fragments.contains(fragment)) {
            fragmentTransaction.add(mContainerViewId, fragment)
        } else {
            fragmentTransaction.show(fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}