package com.peakmain.basicui.activity.home

import com.peakmain.basicui.R
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.widget.lock.LockScreenView
import com.peakmain.ui.widget.lock.LockScreenView.OnLockSuccessListener

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：锁屏的activity
 */
class LockScreenActivity : BaseActivity() {
    private var mLockScreenView: LockScreenView? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_lock_pattern
    }

    override fun initView() {
        mLockScreenView = findViewById(R.id.lock_pattern)
        mNavigationBuilder!!.setTitleText("九宫格解锁").create()
    }

    override fun initData() {
        mLockScreenView!!.setOnLockSuccessListener(object : OnLockSuccessListener {
            override val lockResult: String
                get() = "123456"

            override fun onLockSuccess(pwd: String?) {
                ToastUtils.showShort("密码正确，正在探索BasicUI......")
                finish()
            }
        })
    }
}