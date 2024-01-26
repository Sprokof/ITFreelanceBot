package telegramBot.repository;

import telegramBot.entity.Exchange;

public interface ExchangeRepository {
    Exchange save(Exchange exchange);
    boolean existByName(String name);
    Exchange getByName(String name);

}
