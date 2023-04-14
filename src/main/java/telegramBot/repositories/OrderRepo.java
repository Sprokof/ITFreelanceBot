package telegramBot.repositories;

import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.util.List;

public interface OrderRepo {
    void saveOrder(Order order);
    boolean exist(String orderLink);
    void update(Order order);

    List<Order> getOrdersByLanguage(Language language);
    Order getOrderByLink(String link);
    void deleteOldOrders();
}
