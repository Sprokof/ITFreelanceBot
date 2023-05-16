package telegramBot.dto;

import telegramBot.entity.Order;

public interface OrderBuilder {
    OrderBuilder builder();
    OrderBuilder title(String title);
    OrderBuilder link(String link);
    Order build();
}
