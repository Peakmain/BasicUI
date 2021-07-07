//
// Created by admin on 2021/7/7.
//

#ifndef BASICUI_UI_LOG_H
#define BASICUI_UI_LOG_H

#include <android/log.h>

#ifdef __cplusplus
extern "C" {
#endif

extern android_LogPriority;


#define XH_LOG_TAG "BasicUI"

#define LOGD(fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, XH_LOG_TAG, fmt, ##__VA_ARGS__);
#define LOGI(fmt, ...)  __android_log_print(ANDROID_LOG_INFO,  XH_LOG_TAG, fmt, ##__VA_ARGS__);
#define LOGW(fmt, ...)  __android_log_print(ANDROID_LOG_WARN,  XH_LOG_TAG, fmt, ##__VA_ARGS__);
#define LOGE(fmt, ...)  __android_log_print(ANDROID_LOG_ERROR, XH_LOG_TAG, fmt, ##__VA_ARGS__);


#ifdef __cplusplus
}
#endif

#endif //BASICUI_UI_LOG_H
