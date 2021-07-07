//
// Created by admin on 2021/7/7.
//

#include "JNICommon.h"
#ifdef __cplusplus
extern "C" {
#endif
JavaVM *mJavaVm;
jclass class_HookManager;
jmethodID method_getStack;

JNIEXPORT jint JNI_OnLoad(JavaVM *javaVm, void* reserved) {
    mJavaVm = javaVm;
    JNIEnv *env;
    javaVm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (env) {
        jclass j_hookManager = env->FindClass("com/peakmain/ui/utils/HookManager");
        if (j_hookManager) {
            class_HookManager = (jclass) env->NewGlobalRef(j_hookManager);
            method_getStack = env->GetStaticMethodID(class_HookManager, "getStack",
                                                     "()Ljava/lang/String;");
        }
    }
    return JNI_VERSION_1_6;
};

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {

}


#ifdef __cplusplus
}
#endif

#undef TAG