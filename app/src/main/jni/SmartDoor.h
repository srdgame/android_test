//
// Created by cch on 16-3-11.
//

#ifndef MINIE_SMARTDOOR_H
#define MINIE_SMARTDOOR_H


#include <string>
#include <list>
#include "SmartDoorCallbacks.h"

typedef std::string CardIdentifer;
typedef std::list<CardIdentifer> CardList;

class SmartDoorCallbacks;
class SmartDoor {
public:
    static std::string CardsToJson(CardList);
    static CardList JsonToCards(const std::string& jstr);
public:
    SmartDoor();
    virtual ~SmartDoor();

    int open(const std::string& path, int baudrate, int flags);
    void close();

    CardList getCards();
    int setCards(CardList list);
    int clearCards();


    int addCard(CardIdentifer card);
    int removeCard(CardIdentifer card);

    void run(SmartDoorCallbacks& cb);
private:
    int _fd;
};

#endif //MINIE_SMARTDOOR_H
