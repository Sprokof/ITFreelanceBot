package telegramBot.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Order")
@NoArgsConstructor
public class Order extends BaseEntity {


    @Column(name = "Order_title")
    @Getter
    @Setter
    private String title;

    @Column(name = "Order_link")
    @Getter
    @Setter
    private String link;

    @Column(name = "init_date")
    @Getter
    private LocalDate initDate;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "exchange_id")
    @Getter
    private Exchange exchange;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subscription_id")
    @Getter
    private Subscription subscription;


    public Order(String title, String link) {
       this.title = title;
       this.link = link;
       this.initDate = now();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Order)) return false;
        Order order = (Order) obj;
        return this.title.equals(order.title);
    }

    private LocalDate now(){
        return LocalDate.now();
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
        exchange.addOrder(this);
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        subscription.addOrder(this);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", initDate=" + initDate +
                ", exchange=" + exchange +
                ", subscription=" + subscription +
                '}';
    }
}
