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
public class OrderDto {
    private String title;
    private String link;
    private String details;

    private String exchangeName;
    private String exchangeLink;
    private String subscription;


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


    public Order toEntity(){
        return new Order(this.title, this.link);
    }

    public OrderDto(String title, String link, String details) {
        this.title = title;
        this.link = link;
        this.details = details;
    }
}
