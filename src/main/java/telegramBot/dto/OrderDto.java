package telegramBot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telegramBot.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String title;
    private String link;

    private String exchangeName;
    private String exchangeLink;
    private String subscription;

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

    public String getOrderInfo() {
        return "<a href=" + "'" + this.getExchangeLink() +
                this.getLink() + "'" + ">" + this.getTitle() + "</a>";
    }


}
