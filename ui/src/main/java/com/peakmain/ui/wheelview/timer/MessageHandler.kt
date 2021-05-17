package com.peakmain.ui.wheelview.timer

import android.os.Handler
import android.os.Message
import com.peakmain.ui.wheelview.view.WheelView

/**
 * author:Peakmain
 * createTime:2021/5/17
 * mail:2726449200@qq.com
 * describe： Handler 消息类
 */
class MessageHandler(private val wheelView: WheelView) : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            WHAT_INVALIDATE_LOOP_VIEW -> wheelView.invalidate()
            WHAT_SMOOTH_SCROLL -> wheelView.smoothScroll(
                WheelView.ACTION.FLING
            )
            WHAT_ITEM_SELECTED -> wheelView.onItemSelected()
            else -> {
            }
        }
    }

    companion object {
        const val WHAT_INVALIDATE_LOOP_VIEW = 1000
        const val WHAT_SMOOTH_SCROLL = 2000
        const val WHAT_ITEM_SELECTED = 3000
    }

}