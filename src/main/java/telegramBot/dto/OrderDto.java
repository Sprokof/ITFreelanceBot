package telegramBot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
//TODO rewrite it
public class OrderDto {
    private String title;
    private String link;
    private String details;

    private Exchange exchange;
    private Subscription subscription;


    public OrderDto(String title, String link, Exchange exchange, Subscription subscription) {
        this.title = title;
        this.link = link;
        this.exchange = exchange;
        this.subscription = subscription;
    }


    public Order toEntity(boolean withRelations){
        Order order = new Order(this.title.replaceAll("(&ndash;)", "-"), this.link);
        if (withRelations) {
            order.setExchange(this.exchange);
            order.setSubscription(this.subscription);
        }
        return order;
    }


    public OrderDto(String title, String link, String details) {
        this.title = title;
        this.link = link;
        this.details = details;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", details='" + details + '\'';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.link, this.subscription);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderDto)) return false;
        OrderDto orderDto = (OrderDto) obj;
        return orderDto.title.equals(this.title)
                && orderDto.link.equals(this.link)
                && orderDto.subscription.equals(this.subscription);
    }

}
