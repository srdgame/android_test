//
// Created by cch on 16-3-11.
//

#include <termios.h>
#include <fcntl.h>
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

static speed_t getSpeed(int baudrate)
{
    switch(baudrate) {
        case 0: return B0;
        case 50: return B50;
        case 75: return B75;
        case 110: return B110;
        case 134: return B134;
        case 150: return B150;
        case 200: return B200;
        case 300: return B300;
        case 600: return B600;
        case 1200: return B1200;
        case 1800: return B1800;
        case 2400: return B2400;
        case 4800: return B4800;
        case 9600: return B9600;
        case 19200: return B19200;
        case 38400: return B38400;
        case 57600: return B57600;
        case 115200: return B115200;
        case 230400: return B230400;
        case 460800: return B460800;
        case 500000: return B500000;
        case 576000: return B576000;
        case 921600: return B921600;
        case 1000000: return B1000000;
        case 1152000: return B1152000;
        case 1500000: return B1500000;
        case 2000000: return B2000000;
        case 2500000: return B2500000;
        case 3000000: return B3000000;
        case 3500000: return B3500000;
        case 4000000: return B4000000;
        default: return -1;
    }
}


SmartDoor::SmartDoor() {

}

SmartDoor::~SmartDoor() {

}

int SmartDoor::open(const std::string &path, int baudrate, int flags) {
    LOGI("%s : Open port %s with baudrate %d, flags %d", __FUNCTION__, path.c_str(), baudrate, flags);
    int fd;
    speed_t speed;

    /* Check arguments */
    {
        speed = getSpeed(baudrate);
        if (speed == -1) {
            LOGE("Invalid baudrate");
            return -EINVAL;
        }
    }

    /* Opening device */
    {
        fd = ::open(path.c_str(), O_RDWR | flags);
        LOGD("%s fd = %d", __FUNCTION__, fd);
        if (fd == -EPERM)
        {
            LOGE("Cannot open port");
            return -EPERM;
        }
    }

    /* Configure device */
    {
        struct termios cfg;
        LOGD("Configuring serial port");
        if (tcgetattr(fd, &cfg))
        {
            LOGE("%s calling tcgetattr() failed", __FUNCTION__);
            ::close(fd);
            return -EPERM;
        }

        cfmakeraw(&cfg);
        cfsetispeed(&cfg, speed);
        cfsetospeed(&cfg, speed);

        if (tcsetattr(fd, TCSANOW, &cfg))
        {
            LOGE("%s calling tcsetattr() failed", __FUNCTION__);
            ::close(fd);
            return -EPERM;
        }
    }
    _fd = fd;
    return RC_OK;
}

void SmartDoor::close() {
    LOGD("%s : close fd = %d", __FUNCTION__, _fd);
    ::close(_fd);
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

void SmartDoor::run(SmartDoorCallbacks &cb) {
    cb.onInit(99);
    cb.onClose();
    cb.checkCard("CheckCard No.1");
    cb.onCode(999);
    cb.onPunch("PunchedCard No.1", 99);
}
