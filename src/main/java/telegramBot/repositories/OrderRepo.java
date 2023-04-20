package telegramBot.repositories;

import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;

import java.util.List;
import java.util.Map;

public interface OrderRepo {
    void saveOrder(Order order);
    boolean exist(String orderLink);
    void update(Order order);
    Order getOrderByLink(String link);
    void deleteOldOrders();

    void deleteOrderById(long id);
    List<Order> getOrdersByLanguage(Language language);

    boolean containsOldOrders();

}
