#include <jni.h>
#include "android/log.h"
#include <client/linux/handler/minidump_descriptor.h>
#include <client/linux/handler/exception_handler.h>

#define LOG_TAG "breakpad_crash"
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
JNIEnv *mEnv;

bool MinidumpCallback(const google_breakpad::MinidumpDescriptor &descriptor,
                      void *context,
                      bool succeeded) {

    ALOGD("dump path:%s\n", descriptor.path());
    return succeeded;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_peakmain_breakpad_NativeCrashHandler_initBreakPad(JNIEnv *env, jclass clazz,
                                                           jstring crash_dir) {
    mEnv = env;
    const char *path = env->GetStringUTFChars(crash_dir, 0);
    google_breakpad::MinidumpDescriptor descriptor(path);
    static google_breakpad::ExceptionHandler handler(descriptor, NULL, MinidumpCallback, NULL, true,
                                                     -1);
    env->ReleaseStringUTFChars(crash_dir, path);
}
