package com.peakmain.ui.compress;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * author ：Peakmain
 * createTime：2020/3/27
 * mail:2726449200@qq.com
 * describe：图片等比例压缩  参考luban
 */
public class ImageCompute {
   protected static int computeSize(int srcWidth,int srcHeight){
       srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
       srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
       //拿到最大边和最小边
       int longSide = Math.max(srcWidth, srcHeight);
       int shortSide = Math.min(srcWidth, srcHeight);
       float scale = ((float) shortSide / longSide);
       if (scale <= 1 && scale > 0.5625) {
           if (longSide < 1664) {
               return 1;
           } else if (longSide < 4990) {
               return 2;
           } else if (longSide > 4990 && longSide < 10240) {
               return 4;
           } else {
               return longSide / 1280;
           }
       } else if (scale <= 0.5625 && scale > 0.5) {
           return longSide / 1280 == 0 ? 1 : longSide / 1280;
       } else {
           return (int) Math.ceil(longSide / (1280.0 / scale));
       }
   }
    protected static Bitmap rotatingImage(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
         if(bitmap==null){
             return bitmap;
         }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
