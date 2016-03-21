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

extern "C"
jstring Java_cn_minie_minie_SmartDoor_version(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(_VERSION);
}

static std::map<jobject, SmartDoor*> s_doors;

static SmartDoor* FindDoor(jobject thiz) {
    assert(thiz);
    if (!thiz)
        return nullptr;

    return s_doors[thiz];
}
static void AddDoor(jobject thiz, SmartDoor* door) {
    assert(s_doors[thiz]);
    s_doors[thiz] = door;
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_init(JNIEnv *env, jobject thiz, jstring tty, jint baudrate,
                                        jint flags) {
    auto door = FindDoor(thiz);
    if (!door)
        return 0;
    door = new SmartDoor();

    const char *sztty = env->GetStringUTFChars(tty, NULL);

    int r = door->open(sztty, baudrate, flags);
    if (r == 0)
        AddDoor(thiz, door);

    return r;
}

extern "C"
jstring Java_cn_minie_minie_SmartDoor_getCards(JNIEnv *env, jobject thiz) {
    auto door = FindDoor(thiz);
    if (!door)
        return env->NewStringUTF("SmartDoor object is not initialized");

    CardList list = door->getCards();
    std::string str = door->CardsToJson(list);

    return env->NewStringUTF(str.c_str());
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_setCards(JNIEnv *env, jobject thiz, jstring cards) {
    auto door = FindDoor(thiz);
    if (!door)
        return -10001;

    const char *str = env->GetStringUTFChars(cards, NULL);
    CardList list = door->JsonToCards(str);

    env->ReleaseStringUTFChars(cards, str);

    return door->setCards(list);
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_addCard(JNIEnv *env, jobject thiz, jstring id) {
    auto door = FindDoor(thiz);
    if (!door)
        return -10001;

    const char *str = env->GetStringUTFChars(id, NULL);

    return door->addCard(str);
}

extern "C"
jint Java_cn_minie_minie_SmartDoor_removeCard(JNIEnv *env, jobject thiz, jstring id) {
    auto door = FindDoor(thiz);
    if (!door)
        return -10001;

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
    auto door = FindDoor(thiz);
    if (door)
    {
        SmartDoorCallbacks cb(env, thiz);
        cb.onInit(99);
        cb.onClose();
        cb.checkCard("CheckCard No.1");
        cb.onCode(999);
        cb.onPunch("PunchedCard No.1", 99);
        // Read information.
    }

    sleep(1);
}

