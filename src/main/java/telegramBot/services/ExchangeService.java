package telegramBot.services;

import org.springframework.boot.CommandLineRunner;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.util.List;
import java.util.Set;

public interface ExchangeService extends CommandLineRunner  {
    void update(Exchange exchange);
    Exchange getExchange(telegramBot.enums.Exchange exchange);
    void init();
    List<Order> findNewOrders();

}
