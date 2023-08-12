package telegramBot.repositories;


import telegramBot.enums.BotStatus;

public interface BotStatusRepo {
    String getStatus();
    void setStatus(String status);
}
