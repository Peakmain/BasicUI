package com.peakmain.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.peakmain.ui.R

/**
 * author ：Peakmain
 * createTime：2020/3/1
 * mail:2726449200@qq.com
 * describe：
 */
class AutoDeleteEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), TextWatcher, View.OnFocusChangeListener,
    View.OnClickListener {
    /**
     * 字体大小
     */
    private var mTextSize = 0

    /**
     * 字体颜色
     */
    private var mTextColor = 0

    /**
     * 输入文字以前字体颜色
     */
    private var mHintColor = 0

    /**
     * 输入内容是否居上
     */
    private var isTop = false

    /**
     * 内容居顶部距离
     */
    private var mPaddingTop = 0f

    /**
     * 提示语
     */
    private var mHint: String? = null

    //是否单行展示
    private var isSingle = false

    /**
     * 输入类型
     */
    private var mInputType = 0

    /**
     * 输入长度
     */
    private var mMaxLength = 0
    private lateinit var mEditText: EditText
    private lateinit var mIvDelete: ImageView

    /**
     * 是否有焦点
     */
    private var hasFocus = false

    /**
     * 删除图片的颜色
     */
    private var mDrawableTintList: ColorStateList? = null
    private var mDrawable: Drawable? = null

    /**
     * 解析自定义属性
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.ui_auto_delete_edittext, this)
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoDeleteEditText)
        mTextSize = ta.getDimension(R.styleable.AutoDeleteEditText_adet_text_size, 0f).toInt()
        mTextColor = ta.getColor(R.styleable.AutoDeleteEditText_adet_text_color, 0)
        mHintColor = ta.getColor(R.styleable.AutoDeleteEditText_adet_hint_color, 0)
        isTop = ta.getBoolean(R.styleable.AutoDeleteEditText_adet_isTop, false)
        mPaddingTop = ta.getDimension(R.styleable.AutoDeleteEditText_adet_padding_top, 0f)
        mHint = ta.getString(R.styleable.AutoDeleteEditText_adet_hint)
        isSingle = ta.getBoolean(R.styleable.AutoDeleteEditText_adet_isSingle, false)
        mInputType =
            ta.getInt(R.styleable.AutoDeleteEditText_android_inputType, EditorInfo.TYPE_NULL)
        mMaxLength = ta.getInt(R.styleable.AutoDeleteEditText_adet_max_length, 0)
        if (ta.hasValue(R.styleable.AutoDeleteEditText_adet_tint_color)) {
            mDrawableTintList = ta.getColorStateList(R.styleable.AutoDeleteEditText_adet_tint_color)
        }
        if (ta.hasValue(R.styleable.AutoDeleteEditText_adet_delete_src)) {
            mDrawable = ta.getDrawable(R.styleable.AutoDeleteEditText_adet_delete_src)
        }
        ta.recycle()
        mEditText = findViewById(R.id.et_content)
        mEditText.addTextChangedListener(this)
        mEditText.onFocusChangeListener = this
        mIvDelete = findViewById(R.id.iv_delete)
        mIvDelete.setOnClickListener(this)
        setUp()
    }

    /**
     * 设置属性
     */
    private fun setUp() {
        if (mEditText == null) {
            return
        }
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        mEditText.setTextColor(mTextColor)
        mEditText.setHintTextColor(mHintColor)
        if (isTop) {
            mEditText.gravity = Gravity.TOP
            mEditText.setPadding(0, mPaddingTop.toInt(), 0, 0)
        }
        if (!TextUtils.isEmpty(mHint)) {
            mEditText.hint = mHint
        }
        mEditText.isSingleLine = isSingle
        if (mMaxLength > 0) {
            mEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mMaxLength))
        }
        if (mInputType != 0) {
            mEditText.inputType = mInputType
        }
        if (mDrawableTintList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mIvDelete.imageTintList = mDrawableTintList
            }
        }
        if (mDrawable != null) {
            mIvDelete.setImageDrawable(mDrawable)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (hasFocus) {
            if (s.toString().length > 0) {
                mIvDelete.visibility = View.VISIBLE
            } else {
                mIvDelete.visibility = View.INVISIBLE
            }
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        //如果获取焦点并且有内容则显示删除按钮
        if (hasFocus && !TextUtils.isEmpty(text)) {
            mIvDelete.visibility = View.VISIBLE
        } else {
            mIvDelete.visibility = View.INVISIBLE
        }
    }

    /**
     * 点击事件
     */
    override fun onClick(v: View) {
        if (v.id == R.id.iv_delete) {
            post { mEditText.text.clear() }
        }
    }

    /**
     * 获取EditText
     */
    fun getEditText(): EditText {
        return mEditText
    }

    /**
     * 设置文本
     */
    fun setText(text: CharSequence) {
        mEditText.setText(text)
    }

    /**
     * 返回输入内容
     */
    val text: String
        get() = mEditText.text.toString()

    init {
        init(context, attrs)
    }
}