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
@Table(name = "ORDERS")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Column(name = "ORDER_TITLE")
    @Getter
    @Setter
    private String title;

    @Column(name = "ORDER_LINK")
    @Getter
    @Setter
    private String link;

    @Column(name = "INIT_DATE")
    @Getter
    private LocalDate initDate;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "exchange_id")
    @Getter
    @Setter
    private Exchange exchange;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subscription_id")
    @Getter
    @Setter
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
