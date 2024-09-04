package telegramBot.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Orders")
@NoArgsConstructor
public class Order extends BaseEntity {


    @Column(name = "order_title")
    @Getter
    @Setter
    private String title;

    @Column(name = "order_link")
    @Getter
    @Setter
    private String link;

    @Column(name = "init_date")
    @Getter
    @Setter
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
        if(!(obj instanceof Order order)) return false;
        return this.title.equals(order.title);
    }

    public Order(Order order){
        this.setTitle(order.getTitle());
        this.setLink(order.getLink());
        this.setInitDate(order.getInitDate());
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

    @Override
    public int hashCode() {
        return this.subscription.hashCode();
    }
}
