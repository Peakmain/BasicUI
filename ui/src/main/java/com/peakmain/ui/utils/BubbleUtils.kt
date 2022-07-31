package com.peakmain.ui.utils

import android.graphics.PointF
import kotlin.math.*

object BubbleUtils {

    /**
     * As meaning of method name. 获得两点之间的距离 (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) 开平方
     * Math.sqrt:开平方 Math.pow(p0.y - p1.y, 2):求一个数的平方
     *
     */
    fun getDistanceBetween2Points(p0: PointF, p1: PointF): Float {
        return sqrt((p0.y - p1.y.toDouble()).pow(2.0)
                + (p0.x - p1.x.toDouble()).pow(2.0)).toFloat()
    }

    /**
     * Get point between p1 and p2 by percent. 根据百分比获取两点之间的某个点坐标
     *
     * @param p1 PointF
     * @param p2 PointF
     * @param percent 百分比
     * @return PointF
     */
    fun getPointByPercent(p1: PointF, p2: PointF, percent: Float): PointF {
        return PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(
                percent, p1.y, p2.y))
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     *
     * @param fraction
     * = 1
     * @param start
     * = 10
     * @param end
     * = 3
     * @return
     */
    fun evaluateValue(fraction: Float, start: Number, end: Number): Float {
        // start = 10   end = 2
        //fraction = 0.5
        // result = 10 + (-8) * fraction = 6
        return start.toFloat() + (end.toFloat() - start.toFloat())* fraction
    }

    /**
     * Get the point of intersection between circle and line.
     * 获取通过指定圆心，斜率为lineK的直线与圆的交点。
     *
     * @param pMiddle
     * The circle center point.
     * @param radius
     * The circle radius.
     * @param lineK
     * The slope of line which cross the pMiddle.
     * @return
     */
    fun getIntersectionPoints(pMiddle: PointF, radius: Float,
                              lineK: Double?): Array<PointF?> {
        val points = arrayOfNulls<PointF>(2)

        //高中数学：几何
        val arctan: Float
        val xOffset: Float
        val yOffset: Float
        if (lineK != null) {
            // 计算直角三角形边长
            // 余切函数（弧度）
            arctan = atan(lineK).toFloat()
            // 正弦函数
            xOffset = (sin(arctan.toDouble()) * radius).toFloat()
            // 余弦函数
            yOffset = (cos(arctan.toDouble()) * radius).toFloat()
        } else {
            xOffset = radius
            yOffset = 0f
        }
        points[0] = PointF(pMiddle.x + xOffset, pMiddle.y - yOffset)
        points[1] = PointF(pMiddle.x - xOffset, pMiddle.y + yOffset)
        return points
    }
}