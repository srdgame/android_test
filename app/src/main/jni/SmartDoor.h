//
// Created by cch on 16-3-11.
//

#ifndef MINIE_SMARTDOOR_H
#define MINIE_SMARTDOOR_H


#include <string>
#include <list>

typedef std::string CardIdentifer;
typedef std::list<CardIdentifer> CardList;

class SmartDoor {
public:
    SmartDoor();
    virtual ~SmartDoor();

public:
    int open(const std::string& path, int baudrate, int flags);
    void close();

    CardList getCards();
    int setCards(CardList list);
    int clearCards();
    static std::string CardsToJson(CardList);
    CardList JsonToCards(const std::string& jstr);

    int addCard(CardIdentifer card);
    int removeCard(CardIdentifer card);

};


#endif //MINIE_SMARTDOOR_H
