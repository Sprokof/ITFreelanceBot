package telegramBot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Exchange exchange)) return false;
        return this.link.equals(exchange.link) && this.name.equals(exchange.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.link, this.name);
    }
}
