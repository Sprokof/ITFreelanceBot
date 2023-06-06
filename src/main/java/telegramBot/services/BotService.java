package telegramBot.services;

import org.springframework.boot.CommandLineRunner;
import telegramBot.dto.OrderDto;
import telegramBot.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BotService extends CommandLineRunner {
    void executeNotices(Map<String, Set<OrderDto>> orders);
}
