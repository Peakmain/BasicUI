package com.peakmain.ui.utils;

/**
 * author ：Peakmain
 * createTime：2020/3/15
 * mail:2726449200@qq.com
 * describe：
 */
public class MathUtils {
    /**
     * 两点之间的距离
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }

    /**
     * 转成度
     */
    public static double pointTotoDegrees(double x, double y) {
        return Math.toDegrees(Math.atan2(x, y));
    }

    /**
     * 是否在圆内
     */
    public static boolean checkInRound(float sx, float sy, float r, float x,
                                       float y) {
        // x的平方 + y的平方 开根号 < 半径
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }
}
