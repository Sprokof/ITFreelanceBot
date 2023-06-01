package telegramBot.services;

import org.springframework.boot.CommandLineRunner;
import telegramBot.dto.OrderDto;
import telegramBot.entity.User;

import java.util.List;
import java.util.Map;

public interface BotService extends CommandLineRunner {
    void executeNotices(Map<User, List<OrderDto>> orders);
}
