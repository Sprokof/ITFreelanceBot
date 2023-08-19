package telegramBot.services;

import org.springframework.boot.CommandLineRunner;
import telegramBot.dto.OrderDto;
import telegramBot.entity.User;
import telegramBot.enums.BotStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BotService extends CommandLineRunner {
    BotStatus status();
    void setBotStatus(BotStatus status);
    void executeNotices(Map<String, List<OrderDto>> orders);

}
