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

    @Setter
    @ManyToMany(mappedBy = "subscription")
    private List<User> users;

    public void addOrder(Order order){
        if(this.orders == null) this.orders = new ArrayList<>();
        orders.add(order);
    }

    public List<User> getUsers() {
        if(this.users == null) this.users = new ArrayList<>();
        return users;
    }

}
