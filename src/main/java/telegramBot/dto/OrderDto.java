package telegramBot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telegramBot.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//TODO rewrite it
public class OrderDto implements OrderBuilder {
    private String title;
    private String link;
    private String details;

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

    public OrderDto(String title, String link, String exchangeName,
                    String exchangeLink,
                    String subscription) {
        this.title = title;
        this.link = link;
        this.exchangeName = exchangeName;
        this.exchangeLink = exchangeLink;
        this.subscription = subscription;
    }

    @Override
    public OrderBuilder builder() {
        return this;
    }

    @Override
    public OrderBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public OrderBuilder link(String link) {
        this.link = link;
        return this;
    }

    @Override
    public Order build() {
        return new Order(this.title, this.link);
    }

    public Order doBuild(){
        return builder().
                title(this.title).
                link(this.link).
                build();
    }

    public OrderDto(String title, String link, String details) {
        this.title = title;
        this.link = link;
        this.details = details;
    }
}
