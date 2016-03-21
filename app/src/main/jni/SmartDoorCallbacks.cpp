//
// Created by cch on 16-3-21.
//

#include "SmartDoorCallbacks.h"

SmartDoorCallbacks::SmartDoorCallbacks(JNIEnv *env, jobject thiz) : _env(env), _thiz(thiz) {
}

SmartDoorCallbacks::~SmartDoorCallbacks() {

}

void SmartDoorCallbacks::onInit(int code) {
    jmethodID onInit = GetMethodID("onInit", "(I)V");
    if (onInit != 0) {
        _env->CallVoidMethod(_thiz, onInit, code);
    }
}

void SmartDoorCallbacks::onClose() {

    jmethodID onClose = GetMethodID("onClose", "()V");
    if (onClose != 0) {
        _env->CallVoidMethod(_thiz, onClose);
    }
}

void SmartDoorCallbacks::onPunch(CardIdentifer card, int err) {
    jmethodID method = GetMethodID("onPunch", "(Ljava/lang/String;I)V");
    if (method != 0) {
        jstring jstr = _env->NewStringUTF(card.c_str());
        _env->CallVoidMethod(_thiz, method, jstr, err);
    }
}

bool SmartDoorCallbacks::onCode(int code) {

    jmethodID method = GetMethodID("onCode", "(I)Z");
    if (method != 0) {
        return _env->CallBooleanMethod(_thiz, method, code);
    }
    return false;
}

bool SmartDoorCallbacks::checkCard(CardIdentifer card) {

    jmethodID method = GetMethodID("checkCard", "(Ljava/lang/String;)Z");
    if (method != 0) {
        jstring jstr = _env->NewStringUTF(card.c_str());
        return _env->CallBooleanMethod(_thiz, method, jstr);
    }
    return false;
}

jmethodID SmartDoorCallbacks::GetMethodID(const char *name, const char *sig) {
    jclass thiz_class = _env->GetObjectClass(_thiz);
    return _env->GetMethodID(thiz_class, name, sig);
}
