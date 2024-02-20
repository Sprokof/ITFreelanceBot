package telegramBot.util;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return result.stream()
                .filter(distinctByKey(OrderDto::getLink))
                .filter(distinctByKey(OrderDto::getTitle))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
