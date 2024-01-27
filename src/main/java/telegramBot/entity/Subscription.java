package telegramBot.entity;


import telegramBot.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Subscription")
@Entity
@NoArgsConstructor
public class Subscription extends BaseEntity {


    @Getter
    @Setter
    @Column(name = "lang")
    private String language;

    public Subscription(Language language){
        this.language = language.getName();
    }

    @Getter
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.REFRESH)
    private List<Order> orders;


    public void addOrder(Order order){
        if(this.orders == null) this.orders = new ArrayList<>();
        orders.add(order);
    }


}
