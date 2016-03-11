//
// Created by cch on 16-3-9.
//

//
// Created by cch on 16-3-9.
//

#include <jni.h>
#include <unistd.h>
#include <cstdlib>
#include <string>
#include <map>
#include "version.h"
#include "SmartDoor.h"

std::map<jobject, SmartDoor*> Doors;

extern "C"
jstring Java_cn_minie_minie_SmartDoor_version(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(_VERSION);
}

SmartDoor* FindDoor(jobject thiz) {
    return Doors[thiz];
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_init(JNIEnv *env, jobject thiz) {
    if (Doors[thiz] == NULL) {
        auto p = new SmartDoor();
        int r = p->open("/dev/ttyS1", 9600, 0);
        if (r == 0)
            Doors[thiz] = p;
        else
            delete p;
        return r;
    }
    return 0;
}

extern "C"
jstring Java_cn_minie_minie_SmartDoor_getCards(JNIEnv *env, jobject thiz) {
    auto door = FindDoor(thiz);
    if (door == NULL)
        return nullptr;
    CardList list = door->getCards();
    std::string str = door->CardsToJson(list);
    return env->NewStringUTF(str.c_str());
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_setCards(JNIEnv *env, jobject thiz, jstring cards) {
    auto door = FindDoor(thiz);
    if (door == NULL)
        return -1;
    const char *str = env->GetStringUTFChars(cards, NULL);
    CardList list = door->JsonToCards(str);
    env->ReleaseStringUTFChars(cards, str);

    return list.size();
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_addCard(JNIEnv *env, jobject thiz, jstring id) {
    return 0;
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_removeCard(JNIEnv *env, jobject thiz, jstring id) {
    return 0;
}

extern "C"
jstring Java_cn_minie_minie_SmartDoor_tests(JNIEnv *env, jobject thiz) {
    jclass thiz_class = env->GetObjectClass(thiz);
    jstring jstr = env->NewStringUTF("TestString");

    jmethodID onPunch = env->GetMethodID(thiz_class, "onPunch", "(Ljava/lang/String;)V");
    if (onPunch != 0) {
        env->CallVoidMethod(thiz, onPunch, jstr);
    }
    jmethodID onInit = env->GetMethodID(thiz_class, "onInit", "(I)V");
    if (onInit != 0) {
        env->CallVoidMethod(thiz, onInit, 10);
    }
    jmethodID onClose = env->GetMethodID(thiz_class, "onClose", "()V");
    if (onClose != 0) {
        env->CallVoidMethod(thiz, onClose);
    }

    jmethodID onTest = env->GetMethodID(thiz_class, "onTest", "(Ljava/lang/String;)Ljava/lang/String;");
    if (onTest != 0) {
        jobject result = env->CallObjectMethod(thiz, onTest, jstr);
        const char *str = env->GetStringUTFChars((jstring) result, NULL);
        jstring r = env->NewStringUTF(str);
        env->ReleaseStringUTFChars((jstring) result, str);
        return r;
    }

    return env->NewStringUTF("Exception found");

}

extern "C"
void Java_cn_minie_minie_SmartDoor_Run(JNIEnv *env, jobject thiz) {
    Java_cn_minie_minie_SmartDoor_tests(env, thiz);
    sleep(1);
}

