package telegramBot.entity;


import lombok.Setter;
import telegramBot.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import telegramBot.enums.SubscriptionStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Table(name = "Subscription")
@Entity
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @Getter
    @Column(name = "lang")
    private String language;

    @Getter
    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    public Subscription(Language language, SubscriptionStatus status){
        this.language = language.getName();
        this.status = status;
    }

    @Getter
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.REFRESH)
    private List<Order> orders;


    public void addOrder(Order order){
        if (this.orders == null) {
            this.orders = new LinkedList<>();
        }
        orders.add(order);
    }

}
