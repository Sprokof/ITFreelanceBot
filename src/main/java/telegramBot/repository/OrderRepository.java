package telegramBot.repository;

import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    boolean existByLink(String link);
    boolean existByTitle(String title);
    boolean deleteByExchangeAndDate(Exchange exchange, LocalDate date);
    boolean delete(long id);
    List<Order> getAllByLanguage(Language language);
    int count(Language language);



}
