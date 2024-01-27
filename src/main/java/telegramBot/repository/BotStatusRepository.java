package telegramBot.repository;

import telegramBot.enums.BotStatus;

public interface BotStatusRepository {
    telegramBot.entity.BotStatus get();
    void setStatus(BotStatus status);
}
