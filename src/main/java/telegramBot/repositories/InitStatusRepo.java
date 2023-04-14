package telegramBot.repositories;

import telegramBot.entity.InitStatus;

public interface InitStatusRepo {
    InitStatus getStatus() ;
    void update(InitStatus status);
}
