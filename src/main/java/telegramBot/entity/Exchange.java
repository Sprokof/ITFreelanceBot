package telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Exchange")
@NoArgsConstructor
public class Exchange extends BaseEntity {

    @Getter
    @Setter
    @Column(name = "exchange_name")
    private String name;

    @Getter
    @Setter
    @Column(name = "exchange_link")
    private String link;

    public Exchange(telegramBot.enums.Exchange exchange) {
        this.name = exchange.getName();
        this.link = exchange.getLink();
    }


    public Exchange(String name) {
        this.name = name;
    }

    @Setter
    @Getter
    @OneToMany(mappedBy = "exchange", cascade = CascadeType.REFRESH)
    private List<Order> orders;

    public void addOrder(Order order) {
        if (this.orders == null) this.orders = new ArrayList<>();
        this.orders.add(order);
    }


}
