//
// Created by cch on 16-3-9.
//

//
// Created by cch on 16-3-9.
//

#include <jni.h>
#include <unistd.h>
#include <cstdlib>
#include <cassert>
#include <string>
#include <map>
#include "version.h"
#include "SmartDoor.h"
#include "SmartDoorCallbacks.h"

#include "android/log.h"
#include "error_codes.h"

static const char *TAG="SmartDoor[JNI]";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

extern "C"
jstring Java_cn_minie_minie_SmartDoor_version(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(_VERSION);
}

static std::map<jobject, SmartDoor*> s_doors;

static inline SmartDoor* GetDoor(JNIEnv* env, jobject thiz) {
    jclass thiz_class = env->GetObjectClass(thiz);
    jfieldID jniOjbect = env->GetFieldID(thiz_class, "jniObject", "J");
    if (0 == jniOjbect) {
        LOGE("%s Cannot find jniObject from jobject %ld", __FUNCTION__, (long)thiz);
        return NULL;
    }
    long lObject = env->GetLongField(thiz, jniOjbect);
    return (SmartDoor*)lObject;
}
static inline void SetDoor(JNIEnv* env, jobject thiz, SmartDoor* door) {
    jclass thiz_class = env->GetObjectClass(thiz);
    jfieldID jniOjbect = env->GetFieldID(thiz_class, "jniObject", "J");
    if (jniOjbect != 0)
        env->SetLongField(thiz, jniOjbect, (long)door);
    else
        LOGE("%s Cannot find jniObject from jobject %ld", __FUNCTION__, (long)thiz);
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_init(JNIEnv *env, jobject thiz, jstring tty, jint baudrate,
                                        jint flags) {
    auto door = GetDoor(env, thiz);
    if (door)
        return -RC_ALREADY_INITIALIZED;

    door = new SmartDoor();

    const char *sztty = env->GetStringUTFChars(tty, NULL);

    int r = door->open(sztty, baudrate, flags);
    if (r == 0)
        SetDoor(env, thiz, door);

    return r;
}

extern "C"
void Java_cn_minie_minie_SmartDoor_destroy(JNIEnv *env, jobject thiz) {
    auto door = GetDoor(env, thiz);
    if (door) {
        door->close();
        delete door;
        SetDoor(env, thiz, NULL);
    }
}

extern "C"
jstring Java_cn_minie_minie_SmartDoor_getCards(JNIEnv *env, jobject thiz) {
    auto door = GetDoor(env, thiz);
    if (!door)
        return env->NewStringUTF("SmartDoor object is not initialized");

    CardList list = door->getCards();
    std::string str = door->CardsToJson(list);

    return env->NewStringUTF(str.c_str());
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_setCards(JNIEnv *env, jobject thiz, jstring cards) {
    auto door = GetDoor(env, thiz);
    if (!door)
        return -RC_NOT_INITIALIZED;

    const char *str = env->GetStringUTFChars(cards, NULL);
    CardList list = door->JsonToCards(str);

    env->ReleaseStringUTFChars(cards, str);

    return door->setCards(list);
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_clearCards(JNIEnv *env, jobject thiz) {
    auto door = GetDoor(env, thiz);
    if (door) {
        return door->clearCards();
    }
    return -RC_NOT_INITIALIZED;
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_addCard(JNIEnv *env, jobject thiz, jstring id) {
    auto door = GetDoor(env, thiz);
    if (!door)
        return -RC_NOT_INITIALIZED;

    const char *str = env->GetStringUTFChars(id, NULL);

    return door->addCard(str);
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_removeCard(JNIEnv *env, jobject thiz, jstring id) {
    auto door = GetDoor(env, thiz);
    if (!door)
        return -RC_NOT_INITIALIZED;

    const char *str = env->GetStringUTFChars(id, NULL);

    return door->removeCard(str);
}

extern "C"
jstring Java_cn_minie_minie_SmartDoor_tests(JNIEnv *env, jobject thiz) {
    jclass thiz_class = env->GetObjectClass(thiz);
    jstring jstr = env->NewStringUTF("TestString");

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
    auto door = GetDoor(env, thiz);
    if (door)
    {
        SmartDoorCallbacks cb(env, thiz);
        door->run(cb);
        // Read information.
    }
    else
    {
        LOGE("%s Error: Cannot find Door object from jobject %ld", __FUNCTION__, (long)thiz);
    }

    sleep(1);
}
