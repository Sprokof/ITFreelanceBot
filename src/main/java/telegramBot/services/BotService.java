package telegramBot.services;

import org.springframework.boot.CommandLineRunner;

public interface BotService extends CommandLineRunner {
    void executeNotices();
}
