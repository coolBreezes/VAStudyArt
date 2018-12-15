#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_qing_vasa_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject obj,jintArray jiarr) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
