package telegramBot.repository;


import telegramBot.enums.BotStatus;

public interface BotStatusRepository {
    String getStatus();
    void setStatus(BotStatus status);
}
