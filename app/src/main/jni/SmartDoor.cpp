//
// Created by cch on 16-3-11.
//

#include <string>
#include <sstream>
#include "SmartDoor.h"
#include "json_util.h"

#include "android/log.h"
#include "error_codes.h"

static const char *TAG="SmartDoor[JNI]";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

SmartDoor::SmartDoor() {

}

SmartDoor::~SmartDoor() {

}

int SmartDoor::open(const std::string &path, int baudrate, int flags) {
    LOGI("%s : Open port with baudrate %d, flags %d", __FUNCTION__, baudrate, flags);
    return RC_OK;
}

void SmartDoor::close() {

}

CardList SmartDoor::getCards() {
    CardList list;
    list.push_back("aaaa");
    list.push_back("bbbb");
    list.push_back("ccc");
    return list;
}

int SmartDoor::setCards(CardList list) {
    return RC_OK;
}

int SmartDoor::clearCards() {
    return RC_OK;
}

int SmartDoor::addCard(CardIdentifer card) {
    return RC_OK;
}

int SmartDoor::removeCard(CardIdentifer card) {
    return RC_OK;
}

std::string SmartDoor::CardsToJson(CardList list) {
    std::stringstream ss;
    CardList::iterator ptr = list.begin();
    ss << "[";
    while ( true ) {
        ss << "\"" << escapeJSON(*ptr) << "\"";
         ++ptr;
        if (ptr == list.end())
            break;
        ss << ",";
    }
    ss << "]";
    return ss.str();
}

void split(std::string& s, const std::string& delim,std::list< std::string >* ret)
{
    size_t last = 0;
    size_t index=s.find_first_of(delim,last);
    while (index!=std::string::npos)
    {
        ret->push_back(s.substr(last,index-last));
        last=index+1;
        index=s.find_first_of(delim,last);
    }
    if (index-last>0)
    {
        ret->push_back(s.substr(last,index-last));
    }
}

CardList SmartDoor::JsonToCards(const std::string &jstr) {
    CardList list;

    std::string sub = jstr.substr(jstr.find('[') + 1, jstr.rfind(']') - 1);
    LOGD("Sub: %s", sub.c_str());
    split(sub, ",", &list);

    CardList::iterator ptr = list.begin();
    for (; ptr != list.end(); ++ptr) {
        LOGD("Card: %s", (*ptr).c_str());
    }

    return list;
}
