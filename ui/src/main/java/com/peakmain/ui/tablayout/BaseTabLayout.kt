package com.peakmain.ui.tablayout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.peakmain.ui.R
import com.peakmain.ui.tablayout.ColorTrackTextView
import com.peakmain.ui.utils.SizeUtils.Companion.screenWidth
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/3
 * mail:2726449200@qq.com
 * describe：仿今日头条自定义的TabLayout
 */
abstract class BaseTabLayout<T> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {
    //容器
    private var mLinearLayout: LinearLayout? = null

    //布局的容器
    private var mIndicators: ArrayList<ColorTrackTextView>? = null
    private var mViewPager: ViewPager? = null

    //上一次位置
    private var lastIndex = 0

    //当前位置
    private var currIndex = 0

    //判断是否是点击事件
    private var isTabClick = false

    //当前点击的位置
    private var mPosition = -1
    private val scrollAnimator: ValueAnimator? = null

    //变色的颜色
    private var mChangeColor = 0

    //原始的颜色
    private var mOriginColor = 0

    //是否显示下划线，默认是不显示
    private var mIsShowUnderLine = false
    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseTabLayout)
        mOriginColor = ta.getColor(R.styleable.BaseTabLayout_originColor, Color.BLUE)
        mChangeColor = ta.getColor(R.styleable.BaseTabLayout_changeColor, Color.RED)
        mIsShowUnderLine = ta.getBoolean(R.styleable.BaseTabLayout_isShowUnderLine, false)
        ta.recycle()
        //消除边界反光
        overScrollMode = View.OVER_SCROLL_NEVER
        //垂直方向的水平滚动条是否显示
        isVerticalScrollBarEnabled = false
        //水平方向的水平滚动条是否显示
        isHorizontalScrollBarEnabled = false
        mIndicators = ArrayList()
    }

    /**
     * 初始化可变色的指示器
     *
     * @param bean 换成List<String>也可以，看自己项目需要
    </String> */
    fun initIndicator(bean: List<T>?, viewPager: ViewPager?) {
        mViewPager = viewPager
        if (bean == null) {
            return
        }
        initLinearLayout()
        for (i in bean.indices) {
            // 动态添加颜色跟踪的TextView
            val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            //不可用具体的数字，目的主要是为了适配
            params.leftMargin = screenWidth / 25
            val colorTrackTextView = ColorTrackTextView(context)
            //设置两种颜色
            colorTrackTextView.setOriginColor(mOriginColor)
            colorTrackTextView.setChangeColor(mChangeColor)
            colorTrackTextView.setShowUnderLine(mIsShowUnderLine)
            val name = setTableTitle(bean, i)
            colorTrackTextView.text = name
            colorTrackTextView.layoutParams = params
            //把新的加入LinearLayout容器
            mLinearLayout!!.addView(colorTrackTextView)
            colorTrackTextView.setOnClickListener {
                if (mOnTabItemClickListener != null) {
                    mOnTabItemClickListener!!.onTabItem(i)
                    isTabClick = true
                    mPosition = i
                }
            }
            //添加到集合容器中
            mIndicators!!.add(colorTrackTextView)
        }
        setViewPagerListener(bean)
    }

    protected fun setViewPagerListener(bean: List<T>?) {
        if (null == bean || bean.size == 0) {
            return
        }
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var previousScrollState = 0
            private var scrollState = 0
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                currIndex = position
                if (!isTabClick) {
                    //获取左边
                    val left = mIndicators!![position]
                    //设置朝向
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
                    // 设置进度  positionOffset 是从 0 一直变化到 1
                    left.setCurrentProgress(1 - positionOffset)
                    if (position < bean.size - 1) {
                        //获取右边
                        val right = mIndicators!![position + 1]
                        right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
                        right.setCurrentProgress(positionOffset)
                    }
                } else {
                    //当前位置设置为红色，其他全部设置为黑色
                    for (i in mIndicators!!.indices) {
                        val colorTrackTextView = mIndicators!![i]
                        if (i == position) {
                            colorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
                            colorTrackTextView.setCurrentProgress(1f)
                        } else {
                            colorTrackTextView.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT)
                            colorTrackTextView.setCurrentProgress(0f)
                        }
                    }
                    setScrollPosition(position, positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
                lastIndex = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                previousScrollState = scrollState
                scrollState = state
                if (!isTabClick) {
                    if (currIndex != mIndicators!!.size - 1 && currIndex != 0) {
                        scrollToChild(currIndex)
                    }
                }
                if (state == 0) {
                    isTabClick = false
                }
            }
        })
    }

    protected fun setScrollPosition(position: Int, positionOffset: Float) {
        val roundedPosition = Math.round(position.toFloat() + positionOffset)
        if (roundedPosition >= 0 && roundedPosition < mLinearLayout!!.childCount) {
            if (scrollAnimator != null && scrollAnimator.isRunning) {
                scrollAnimator.cancel()
            }
            smoothScrollTo(calculateScrollxForTab(position, positionOffset), 0)
        }
    }

    private fun calculateScrollxForTab(position: Int, positionOffset: Float): Int {
        val selectedChild = mLinearLayout!!.getChildAt(position)
        val nextChild = if (position + 1 < mLinearLayout!!.childCount) mLinearLayout!!.getChildAt(position + 1) else null
        val selectedWidth = selectedChild?.width ?: 0
        val nextWidth = nextChild?.width ?: 0
        val scrollBase = selectedChild!!.left + selectedWidth / 2 - this.width / 2
        val scrollOffset = ((selectedWidth + nextWidth).toFloat() * 0.5f * positionOffset).toInt()
        return if (ViewCompat.getLayoutDirection(this) == 0) scrollBase + scrollOffset else scrollBase - scrollOffset
    }

    private fun scrollToChild(position: Int) {
        //获取屏幕的宽度
        val screenWidth = screenWidth
        //计算控件居正中时距离左侧屏幕的距离
        val middleLeftPosition = (screenWidth - mLinearLayout!!.getChildAt(position).width) / 2
        //正中间位置需要向左偏移的距离
        val offset = mLinearLayout!!.getChildAt(position).left - middleLeftPosition
        smoothScrollTo(offset, 0)
    }

    /**
     * 点击onTable事件的回掉
     */
    interface OnTabItemClickListener {
        fun onTabItem(postition: Int)
    }

    private var mOnTabItemClickListener: OnTabItemClickListener? = null
    fun setOnTabItemClickListener(onTabItemClickListener: OnTabItemClickListener?) {
        mOnTabItemClickListener = onTabItemClickListener
    }

    /**
     * 显示的名字
     */
    abstract fun setTableTitle(bean: List<T>?, position: Int): String

    /**
     * 初始化标签容器
     */
    private fun initLinearLayout() {
        mLinearLayout = LinearLayout(context)
        mLinearLayout!!.orientation = LinearLayout.HORIZONTAL
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mLinearLayout!!.layoutParams = params
        addView(mLinearLayout)
    }

    init {
        init(context, attrs)
    }
}