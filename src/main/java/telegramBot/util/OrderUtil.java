package telegramBot.util;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderUtil {

    public static OrderDto toDto(Order order){
        String title = order.getTitle();
        String link = order.getLink();
        String exchangeName = order.getExchange().getName();
        String exchangeLink = order.getExchange().getLink();
        String subscription = order.getSubscription().getLanguage();
        return new OrderDto(title, link, exchangeName, exchangeLink, subscription);
    }

    public static List<OrderDto> toDtos(List<Order> orders) {
        List<OrderDto> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(toDto(order));
        }
        return result;
    }


}
