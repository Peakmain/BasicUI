#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_peakmain_basicui_fragment_UtilsFragment_testNativeBug(
        JNIEnv *env,
        jobject /* this */) {
    printf(NULL);
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}