package telegramBot.util;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OrderUtil {
    public static OrderDto toDto(Order order){
        String title = order.getTitle();
        String link = order.getLink();
        return new OrderDto(title, link, order.getExchange(), order.getSubscription());
    }


    public static List<OrderDto> toDtos(List<Order> orders) {
        return orders.stream()
                .map(OrderUtil::toDto)
                .filter(distinctByKey(OrderDto::getLink))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


}
