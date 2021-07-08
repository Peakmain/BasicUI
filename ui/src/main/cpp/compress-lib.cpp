#include <string.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <stdio.h>
#include <setjmp.h>
#include <math.h>
#include <stdint.h>
#include <time.h>
#include "FileLogger.h"

//统一编译方式
extern "C" {
#include "jpeglib.h"
#include "cdjpeg.h"        /* Common decls for cjpeg/djpeg applications */
#include "jversion.h"        /* for version message */
#include "jconfig.h"
}

#include "ui_log.h"

#define true 1
#define false 0

typedef uint8_t BYTE;

// error 结构体
char *error;
struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};

typedef struct my_error_mgr *my_error_ptr;

METHODDEF(void)
my_error_exit(j_common_ptr cinfo) {
    my_error_ptr myerr = (my_error_ptr) cinfo->err;
    (*cinfo->err->output_message)(cinfo);
    error = (char *) myerr->pub.jpeg_message_table[myerr->pub.msg_code];
    LOGE("jpeg_message_table[%d]:%s", myerr->pub.msg_code,
         myerr->pub.jpeg_message_table[myerr->pub.msg_code]);
    longjmp(myerr->setjmp_buffer, 1);
}

int generateJPEG(BYTE *data, int w, int h, int quality,
                 const char *outfilename, jboolean optimize) {

    // 结构体相当于Java类
    struct jpeg_compress_struct jcs;

    //当读完整个文件的时候就会回调my_error_exit这个退出方法。
    struct my_error_mgr jem;
    jcs.err = jpeg_std_error(&jem.pub);
    jem.pub.error_exit = my_error_exit;
    // setjmp是一个系统级函数，是一个回调。
    if (setjmp(jem.setjmp_buffer)) {
        return 0;
    }

    //初始化jsc结构体
    jpeg_create_compress(&jcs);
    //打开输出文件 wb 可写  rb 可读
    FILE *f = fopen(outfilename, "wb");
    if (f == NULL) {
        return 0;
    }
    //设置结构体的文件路径，以及宽高
    jpeg_stdio_dest(&jcs, f);
    jcs.image_width = w;
    jcs.image_height = h;

    // /* TRUE=arithmetic coding, FALSE=Huffman */
    jcs.arith_code = false;
    int nComponent = 3;
    /* 颜色的组成 rgb，三个 # of color components in input image */
    jcs.input_components = nComponent;
    //设置颜色空间为rgb
    jcs.in_color_space = JCS_RGB;
    ///* Default parameter setup for compression */
    jpeg_set_defaults(&jcs);
    //是否采用哈弗曼
    jcs.optimize_coding = optimize;
    //设置质量
    jpeg_set_quality(&jcs, quality, true);
    //开始压缩
    jpeg_start_compress(&jcs, TRUE);

    JSAMPROW row_pointer[1];
    int row_stride;
    row_stride = jcs.image_width * nComponent;
    while (jcs.next_scanline < jcs.image_height) {
        //得到一行的首地址
        row_pointer[0] = &data[jcs.next_scanline * row_stride];
        jpeg_write_scanlines(&jcs, row_pointer, 1);
    }
    // 压缩结束
    jpeg_finish_compress(&jcs);
    // 销毁回收内存
    jpeg_destroy_compress(&jcs);
    //关闭文件
    fclose(f);
    return 1;
}


extern "C"
jint Java_com_peakmain_ui_compress_CompressUtils_compressBitmap(JNIEnv *env,
                                                                jclass thiz, jobject bitmap,
                                                                jint quality,
                                                                jstring fileNameStr) {
    // 1. 解析RGB
    // 1.1 获取bitmap信息  w，h，format  Android的Native要有了解
    AndroidBitmapInfo info;
    // java你调用完方法往往返回的是对象，而C往往是参数
    AndroidBitmap_getInfo(env, bitmap, &info);
    // 从地址获取值
    int bitmap_height = info.height;
    int bitmap_width = info.width;
    int bitmap_format = info.format;
    if (bitmap_format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        // argb
        return -1;
    }

    LOGE("bitmap_height = %d,bitmap_width = %d,", bitmap_height, bitmap_width);

    // 1.2 把bitmap解析到数组中，数组中保存的是rgb -> YCbCr
    // 1.2.1 锁定画布
    BYTE *pixel_color;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &pixel_color);

    // 1.2.2 解析数据，定义一些变量
    BYTE *data;
    BYTE r, g, b;
    // 申请一块内存 = 宽*高*3
    data = (BYTE *) malloc(bitmap_width * bitmap_height * 3);
    // 数组指针指向的是数组首地址，因为这块内存要释放所以先保存一下
    BYTE *tempData;
    tempData = data;

    // 一个一个像素解析保存到data
    int i = 0;
    int j = 0;
    int color;
    for (i = 0; i < bitmap_height; ++i) {
        for (j = 0; j < bitmap_width; ++j) {
            // 获取二位数组的每一个像素信息的首地址
            color = *((int *) pixel_color);
            // 把 rgb 取出来
            r = ((color & 0x00FF0000) >> 16);
            g = ((color & 0x0000FF00) >> 8);
            b = (color & 0x000000FF);

            // 保存到data里面去
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;

            data = data + 3;
            // 一个像素点包括argb四个值，每+4下就是取下一个像素点
            pixel_color += 4;
        }
    }

    // 1.2.3 解锁画布
    AndroidBitmap_unlockPixels(env, bitmap);

    // 1.2.4 还差一个参数，jstring -> char*
    char *file_name = (char *) env->GetStringUTFChars(fileNameStr, NULL);
    LOGE("file_name = %s", file_name);

    // 2.调用第三方的提供好的方法   赋值
    int result = generateJPEG(tempData, bitmap_width, bitmap_height, quality, file_name, true);
    LOGE("result = %d", result);
    // 3.一定要回收内存
    free(tempData);
    env->ReleaseStringUTFChars(fileNameStr, file_name);
    // 释放bitmap,调用bitmap的recycle
    // 3.2 获取对象的class
    jclass obj_clazz = env->GetObjectClass(bitmap);
    // 3.3 通过class获取方法id
    jmethodID method_id = env->GetMethodID(obj_clazz, "recycle", "()V");//()V代表void方法
    // 3.4 调用方法释放Bitmap
    env->CallVoidMethod(bitmap, method_id);

    LOGE("result = %d", result);

    // 4.返回结果
    if (result == 0) {
        return -1;
    }

    return 1;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_peakmain_ui_utils_file_FileLogger_nativeLogFileCreate(JNIEnv *env, jobject thiz,
                                                               jstring logPath_, jint maxFileSize) {
    const char *logPath = env->GetStringUTFChars(logPath_, NULL);

    FileLogger *fileLogger = new FileLogger(logPath, maxFileSize);

    env->ReleaseStringUTFChars(logPath_, logPath);

    return reinterpret_cast<jlong>(fileLogger);

}
extern "C"
JNIEXPORT void JNICALL
Java_com_peakmain_ui_utils_file_FileLogger_nativeWriteData(JNIEnv *env, jobject thiz,
                                                           jlong nativePtr, jbyteArray data_,
                                                           jint dataLen) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);

    FileLogger *fileLogger = reinterpret_cast<FileLogger *>(nativePtr);
    if (fileLogger != NULL) {
        fileLogger->writeData((char *) data, dataLen);
    }

    env->ReleaseByteArrayElements(data_, data, 0);

}
extern "C"
JNIEXPORT jint JNICALL
Java_com_peakmain_ui_utils_file_FileLogger_nativeLogFileLength(JNIEnv *env, jobject thiz,
                                                               jlong nativePtr) {
    FileLogger *fileLogger = (FileLogger *) nativePtr;
    if (fileLogger != NULL) {
        return fileLogger->dataPos;
    }
    return 0;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_peakmain_ui_utils_file_FileLogger_nativeRelease(JNIEnv *env, jobject thiz,
                                                         jlong nativePtr) {
    FileLogger *fileLogger = (FileLogger *) nativePtr;
    if (fileLogger != NULL) {
        delete (fileLogger);
    }
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_peakmain_ui_utils_file_FileLogger_nativeReadLog(JNIEnv *env, jobject thiz,
                                                         jlong nativePtr) {

    FileLogger *fileLogger = (FileLogger *) nativePtr;
    if (fileLogger != NULL) {
        char *mmapPtr = fileLogger->readLog();
        jstring data=env->NewStringUTF(mmapPtr);
        return data;
    }
}