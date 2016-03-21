//
// Created by cch on 16-3-21.
//

#ifndef MINIE_SMARTDOORCALLBACKS_H
#define MINIE_SMARTDOORCALLBACKS_H

#include <jni.h>
#include "SmartDoor.h"


class SmartDoorCallbacks {
public:
    SmartDoorCallbacks(JNIEnv *env, jobject thiz);
    ~SmartDoorCallbacks();

public:
    void onInit(int code);
    void onClose();
    void onPunch(CardIdentifer card, int err);
    bool onCode(int code);
    bool checkCard(CardIdentifer card);
private:
    jmethodID GetMethodID(const char* name, const char* sign);
private:
    JNIEnv* _env;
    jobject _thiz;
};


#endif //MINIE_SMARTDOORCALLBACKS_H
