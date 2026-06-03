package com.peakmain.basicui.activity.utils

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.toast.TopToastUtils

class TopToastActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    private lateinit var mBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null

    override fun getLayoutId(): Int = R.layout.basic_linear_recycler_view

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        mNavigationBuilder
            ?.setTitleText("顶部Toast提示")
            ?.create()
    }

    override fun initData() {
        mBean = ArrayList()
        mBean.add("[Success] 默认时长提示")
        mBean.add("[Success] 自定义时长(5s)")
        mBean.add("[Error] 默认时长提示")
        mBean.add("[Error] 自定义时长(5s)")
        mBean.add("[Error] Debug下异常信息拦截")
        mBean.add("[Action] 默认时长提示")
        mBean.add("[Action] 自定义时长(5s)")
                mBean.add("[Action+Finish] 提示并关闭页面(2s)")
        mBean.add("[Dialog] Success 默认时长")
        mBean.add("[Dialog] Success 自定义时长(5s)")
        mBean.add("[Dialog] Error 默认时长")
        mBean.add("[Dialog] Error 自定义时长(5s)")
        mBean.add("[Dialog] Error Debug下异常拦截")
        mBean.add("[Dialog] Action 默认时长")
        mBean.add("[Dialog] Action 自定义时长(5s)")

        mAdapter = BaseRecyclerStringAdapter(this, mBean)
        mRecyclerView?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        mRecyclerView?.adapter = mAdapter
        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> TopToastUtils.showSuccessToast("操作成功")
                    1 -> TopToastUtils.showSuccessToast("自定义时长提示", 5000)
                    2 -> TopToastUtils.showErrorToast("操作失败")
                    3 -> TopToastUtils.showErrorToast("自定义错误提示", 5000)
                    4 -> TopToastUtils.showErrorToast("java.lang.NullPointerException")
                    5 -> TopToastUtils.showActionToast("这是一条普通提示")
                    6 -> TopToastUtils.showActionToast("自定义普通提示", 5000)
                    7 -> TopToastUtils.showActionToastWithFinish("操作完成，即将关闭", 2000)
                    8 -> TopToastUtils.showSuccessDialogToast("Dialog操作成功")
                    9 -> TopToastUtils.showSuccessDialogToast("Dialog自定义时长", 5000)
                    10 -> TopToastUtils.showErrorDialogToast("Dialog操作失败")
                    11 -> TopToastUtils.showErrorDialogToast("Dialog自定义错误", 5000)
                    12 -> TopToastUtils.showErrorDialogToast("java.lang.NullPointerException")
                    13 -> TopToastUtils.showActionDialogToast("Dialog普通提示")
                    14 -> TopToastUtils.showActionDialogToast("Dialog自定义普通提示", 5000)
                }
            }
        })
    }
}