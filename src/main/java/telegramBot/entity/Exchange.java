package telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import telegramBot.enums.Language;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EXCHANGES")
@NoArgsConstructor
public class Exchange {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(name = "EXCHANGE_NAME")
    private String name;

    @Getter
    @Setter
    @Column(name = "EXCHANGE_LINK")
    private String link;

    public Exchange(telegramBot.enums.Exchange exchange) {
        this.name = exchange.getName();
        this.link = exchange.getLink();
    }


    public Exchange(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "exchange")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter
    @Getter
    private List<Order> orders;

    public void addOrder(Order order) {
        if (this.orders == null) this.orders = new ArrayList<>();
        this.orders.add(order);
        order.setExchange(this);
    }


}
