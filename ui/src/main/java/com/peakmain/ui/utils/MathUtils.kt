package com.peakmain.ui.utils

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：
 */
object MathUtils {
    /**
     * 两点之间的距离
     */
    @JvmStatic
    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2))
    }

    /**
     * 转成度
     */
    fun pointTotoDegrees(x: Double, y: Double): Double {
        return Math.toDegrees(Math.atan2(x, y))
    }

    /**
     * 是否在圆内
     */
    @JvmStatic
    fun checkInRound(sx: Float, sy: Float, r: Float, x: Float,
                     y: Float): Boolean {
        // x的平方 + y的平方 开根号 < 半径
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y).toDouble()) < r
    }
}