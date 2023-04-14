package telegramBot.repositories;

import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.util.List;

public interface ExchangeRepo {
    void update(Exchange exchange);
    boolean isExist(String exchangeName);
    Exchange getExchangeByName(String name);

}
