package telegramBot.services;

import org.springframework.boot.CommandLineRunner;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface OrderService extends CommandLineRunner {
    boolean saveIfNotExist(Order order);
    void saveOrder(Order order);
    void updateOrder(Order order);
    boolean orderExist(Order order);
    void deleteOldOrders();
    String getLatestOrdersMessage(Update update);



}
