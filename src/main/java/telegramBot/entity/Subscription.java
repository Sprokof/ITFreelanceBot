package telegramBot.entity;


import telegramBot.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Table(name = "SUBSCRIPTIONS")
@Entity
@NoArgsConstructor
public class Subscription {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(name = "SUB_LANGUAGE")
    private String language;

    public Subscription(Language language){
        this.language = language.getName();
    }


    @ManyToMany()
    @JoinTable(name = "USERS_SUBSCRIPTIONS",
    joinColumns = @JoinColumn(name = "subscription_id"),
    inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users;

    @OneToMany(mappedBy = "subscription")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Getter
    private List<Order> orders;

    public void addOrder(Order order){
        if(this.orders == null) this.orders = new LinkedList<>();
        this.orders.add(order);
        order.setSubscription(this);
    }

    public List<User> getUsers() {
        if(this.users == null) this.users = new ArrayList<>();
        return users;
    }
}
