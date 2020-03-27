package com.peakmain.ui.utils;

import android.content.Context;
import android.graphics.PointF;

public class BubbleUtils {



    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return SizeUtils.dp2px( context,25);
    }

    /**
     * As meaning of method name. 获得两点之间的距离 (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) 开平方
     * Math.sqrt:开平方 Math.pow(p0.y - p1.y, 2):求一个数的平方
     *
     */
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        float distance = (float) Math.sqrt(Math.pow(p0.y - p1.y, 2)
                + Math.pow(p0.x - p1.x, 2));
        return distance;
    }

    /**
     * Get point between p1 and p2 by percent. 根据百分比获取两点之间的某个点坐标
     *
     * @param p1 PointF
     * @param p2 PointF
     * @param percent 百分比
     * @return PointF
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(
                percent, p1.y, p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     *
     * @param fraction
     *            = 1
     * @param start
     *            = 10
     * @param end
     *            = 3
     * @return
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        // start = 10   end = 2
        //fraction = 0.5
        // result = 10 + (-8) * fraction = 6
        return start.floatValue() + (end.floatValue() - start.floatValue())
                * fraction;
    }

    /**
     * Get the point of intersection between circle and line.
     * 获取通过指定圆心，斜率为lineK的直线与圆的交点。
     *
     * @param pMiddle
     *            The circle center point.
     * @param radius
     *            The circle radius.
     * @param lineK
     *            The slope of line which cross the pMiddle.
     * @return
     */
    public static PointF[] getIntersectionPoints(PointF pMiddle, float radius,
                                                 Double lineK) {
        PointF[] points = new PointF[2];

        //高中数学：几何
        float arctan, xOffset = 0, yOffset = 0;
        if (lineK != null) {
            // 计算直角三角形边长
            // 余切函数（弧度）
            arctan = (float) Math.atan(lineK);
            // 正弦函数
            xOffset = (float) (Math.sin(arctan) * radius);
            // 余弦函数
            yOffset = (float) (Math.cos(arctan) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
        points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);

        return points;
    }
}
