//
// Created by cch on 16-3-21.
//

#ifndef MINIE_SMARTDOORCALLBACKS_H
#define MINIE_SMARTDOORCALLBACKS_H

#include <jni.h>


class SmartDoorCallbacks {
public:
    SmartDoorCallbacks(JNIEnv *env, jobject thiz);
    ~SmartDoorCallbacks();

public:
    void onInit(int code);
    void onClose();
    void onPunch(const char* card, int err);
    bool onCode(int code);
    bool checkCard(const char* card);
private:
    jmethodID GetMethodID(const char* name, const char* sig);
private:
    JNIEnv* _env;
    jobject _thiz;
};


#endif //MINIE_SMARTDOORCALLBACKS_H
