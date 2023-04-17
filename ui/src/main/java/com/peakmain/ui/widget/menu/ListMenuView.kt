package com.peakmain.ui.widget.menu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.peakmain.ui.R
import com.peakmain.ui.adapter.menu.BaseMenuAdapter
import com.peakmain.ui.adapter.menu.MenuObserver
import com.peakmain.ui.ext.addOnGlobalLayoutListener
import com.peakmain.ui.utils.SizeUtils

/**
 * author: Peakmain
 * createTime：2020/3/4
 * mail:2726449200@qq.com
 * describe：
 */
class ListMenuView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(mContext, attrs, defStyleAttr), View.OnClickListener {

    // 1.1 创建头部用来存放 Tab
    private var mMenuTabView: LinearLayout? = null

    // 1.2 创建 FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
    private var mMenuMiddleView: FrameLayout? = null

    // 阴影
    private var mShadowView: View? = null

    // 创建菜单用来存放菜单内容
    private var mMenuContainerView: FrameLayout? = null

    // 阴影的颜色
    private val mShadowColor = -0x77777778
    private var mAdapter: BaseMenuAdapter? = null
    private var mMenuContainerHeight = 0
    private val mDurationTime = 350
    private var mCurrentPosition = -1 //当前位置

    // 动画是否在执行
    private var mAnimatorExecute = false

    /**
     * 初始化控件
     */
    private fun initLayout() {
        //整体是个垂直布局
        orientation = VERTICAL
        //创建头部用来存放Tab
        mMenuTabView = LinearLayout(mContext)
        mMenuTabView!!.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        addView(mMenuTabView)
        // 1.2 创建 FrameLayout 用来存放 = 阴影（View） + 菜单内容布局(FrameLayout)
        mMenuMiddleView = FrameLayout(mContext)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, 0)
        params.weight = 1f
        mMenuMiddleView!!.layoutParams = params
        addView(mMenuMiddleView)
        // 创建阴影 可以不用设置 LayoutParams 默认就是 MATCH_PARENT ，MATCH_PARENT
        mShadowView = View(mContext)
        mShadowView!!.setBackgroundColor(mShadowColor)
        mShadowView!!.visibility = View.GONE
        mShadowView!!.alpha = 0f
        mShadowView!!.id = R.id.menu_shadow_view
        mMenuMiddleView!!.addView(mShadowView)
        mShadowView!!.setOnClickListener(this)

        //创建菜单用来存放菜单内容
        mMenuContainerView = FrameLayout(mContext)
        mMenuContainerView!!.setBackgroundColor(Color.WHITE)
        mMenuMiddleView!!.addView(mMenuContainerView)
        val mMenuParams = mMenuContainerView!!.layoutParams
        mMenuContainerHeight = (SizeUtils.screenHeight * 75f / 100).toInt()
        mMenuParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mMenuContainerView!!.layoutParams = mMenuParams
        mMenuContainerView!!.translationY = -mMenuParams.height.toFloat()
        mMenuContainerView!!.setOnClickListener(this)
    }

    /**
     * 具体的观察者
     */
    private inner class AdapterMenuObserver : MenuObserver() {
        override fun closeMenu() {
            this@ListMenuView.closeMenu()
        }
    }

    private var mObserver: AdapterMenuObserver? = null
    fun setAdapter(adapter: BaseMenuAdapter?) {
        if (mAdapter != null && mObserver != null) {
            mAdapter!!.unregisterDataSetObserver(mObserver!!)
        }
        mAdapter = adapter
        if (adapter == null) {
            throw RuntimeException("adapter is null")
        }
        mObserver = AdapterMenuObserver()
        mAdapter!!.registerDataSetObserver(mObserver)
        val count = adapter.count
        for (i in 0 until count) {
            // 获取菜单的Tab
            val tabView = adapter.getView(i, mMenuTabView)
            val params = tabView!!.layoutParams as LayoutParams
            params.weight = 1f
            tabView.layoutParams = params
            mMenuTabView!!.addView(tabView)
            //设置点击事件
            setTabClick(tabView, i)
            //获取菜单内容
            val menuView = mAdapter!!.getMenuView(i, mMenuContainerView!!)
            menuView.visibility = View.GONE
            mMenuContainerView!!.addView(menuView)
        }
    }

    private var mOldMenuHeight = 0f
    private fun setTabClick(tabView: View?, position: Int) {
        tabView!!.setOnClickListener {
            if (mCurrentPosition == -1) {
                openMenu(tabView, position)
                return@setOnClickListener
            }
            //已经打开了
            if (mCurrentPosition == position) { //如果点击的是同一个位置则关闭
                closeMenu()
                return@setOnClickListener
            }
            //切换显示
            mMenuContainerView?.layoutParams?.height=ViewGroup.LayoutParams.WRAP_CONTENT
            var currentMenu = mMenuContainerView!!.getChildAt(mCurrentPosition)
            currentMenu.visibility = View.GONE
            var height = currentMenu.layoutParams.height
            mOldMenuHeight = when (height) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    currentMenu.measuredHeight.toFloat()
                }
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    mMenuContainerHeight.toFloat()
                }
                else -> currentMenu.layoutParams.height.toFloat()
            }
            mAdapter!!.closeMenu(mMenuTabView!!.getChildAt(mCurrentPosition))
            mCurrentPosition = position
            currentMenu = mMenuContainerView!!.getChildAt(mCurrentPosition)
            currentMenu.visibility = View.VISIBLE
            mAdapter!!.openMenu(mMenuTabView!!.getChildAt(mCurrentPosition))
            height = currentMenu.layoutParams.height
            var endHeight = when (height) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    currentMenu.measuredHeight
                }
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    mMenuContainerHeight
                }
                else -> {
                    currentMenu.layoutParams.height
                }
            }
            if (endHeight == 0) {
                currentMenu.addOnGlobalLayoutListener {
                    endHeight = it
                    if (endHeight >= mMenuContainerHeight) {
                        endHeight = mMenuContainerHeight
                        mMenuContainerView?.layoutParams?.height = endHeight
                        mMenuContainerView?.requestLayout()
                    }
                    translationMenu(
                        mMenuContainerView,
                        mOldMenuHeight.toInt(),
                        endHeight
                    )
                }
                return@setOnClickListener
            }
            if (endHeight >= mMenuContainerHeight) {
                endHeight = mMenuContainerHeight
                mMenuContainerView?.layoutParams?.height = endHeight
                mMenuContainerView?.requestLayout()
            }
            translationMenu(
                mMenuContainerView,
                mOldMenuHeight.toInt(),
                endHeight
            )
        }
    }

    private fun translationMenu(view: FrameLayout?, startHeight: Int, endHeight: Int) {
        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.addUpdateListener {
            val height = it.animatedValue as Int
            view?.layoutParams?.height = height
            view?.requestLayout()
        }
        animator.start()
    }

    fun closeMenu() {
        if (mAnimatorExecute) {
            return
        }
        //设置阴影不可见
        mShadowView!!.visibility = View.GONE
        // 获取当前位置显示当前菜单，菜单是加到了菜单容器
        val menuView = mMenuContainerView!!.getChildAt(mCurrentPosition)
        var height = menuView?.layoutParams?.height
        if (height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = menuView.measuredHeight
        } else if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = mMenuContainerHeight
        }

        if (height == 0) {
            menuView.addOnGlobalLayoutListener { viewHeight ->
                height = viewHeight
                height?.also {
                    if (it > mMenuContainerHeight) {
                        height = mMenuContainerHeight
                        mMenuContainerView?.layoutParams?.height = height
                        mMenuContainerView?.requestLayout()
                    }
                }
                closeMenuAnimator(height ?: 0, menuView)
            }
            return
        }
        closeMenuAnimator(height ?: 0, menuView)
    }

    private fun closeMenuAnimator(height: Int, menuView: View) {
        //位移动画
        val translationAnimation = ObjectAnimator.ofFloat(
            mMenuContainerView,
            context.getString(R.string.translationY),
            0f,
            -height.toFloat()
        )
        translationAnimation.duration = mDurationTime.toLong()
        translationAnimation.start()
        //阴影的透明度变化
        val rotationAnimation =
            ObjectAnimator.ofFloat(mShadowView, context.getString(R.string.alpha), 1f, 0f)
        rotationAnimation.duration = mDurationTime.toLong()
        rotationAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentPosition = -1
                mAnimatorExecute = false
                menuView.visibility = GONE
                mMenuContainerView?.apply {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                mAnimatorExecute = true
                mAdapter!!.closeMenu(mMenuTabView!!.getChildAt(mCurrentPosition))
            }
        })
        rotationAnimation.start()
    }

    /**
     * 打开菜单
     */
    private fun openMenu(tabView: View?, position: Int) {
        if (mAnimatorExecute) {
            return
        }
        //设置阴影为可见
        mShadowView!!.visibility = View.VISIBLE

        // 获取当前位置显示当前菜单，菜单是加到了菜单容器
        val menuView = mMenuContainerView!!.getChildAt(position)
        menuView.visibility = View.VISIBLE

        var height = menuView?.layoutParams?.height
        if (height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = menuView.measuredHeight
        } else if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = mMenuContainerHeight
        }
        height = height ?: 0
        if (height == 0) {
            menuView.addOnGlobalLayoutListener {
                height = it
                if (height!! >= mMenuContainerHeight) {
                    height = mMenuContainerHeight
                    mMenuContainerView?.layoutParams?.height = height
                    mMenuContainerView?.requestLayout()
                }
                openMenuAnimator(height ?: 0, position, tabView)
            }
            return
        }
        if (height!! >= mMenuContainerHeight) {
            height = mMenuContainerHeight
            mMenuContainerView?.layoutParams?.height = height
            mMenuContainerView?.requestLayout()
        }

        openMenuAnimator(height!!, position, tabView)
    }

    private fun openMenuAnimator(
        height: Int,
        position: Int,
        tabView: View?
    ) {
        //位移动画
        val translationAnimation = ObjectAnimator.ofFloat(
            mMenuContainerView,
            context.getString(R.string.translationY),
            -height.toFloat(),
            0f
        )
        translationAnimation.duration = mDurationTime.toLong()
        translationAnimation.start()
        //阴影的透明度变化
        val rotationAnimation =
            ObjectAnimator.ofFloat(mShadowView, context.getString(R.string.alpha), 0f, 1f)
        rotationAnimation.duration = mDurationTime.toLong()
        rotationAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mCurrentPosition = position
                mAnimatorExecute = false
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                mAnimatorExecute = true
                mAdapter!!.openMenu(tabView!!)
            }
        })
        rotationAnimation.start()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.menu_shadow_view) {
            closeMenu()
        }
    }

    init {
        initLayout()
    }
}