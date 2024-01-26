package telegramBot.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.repository.EntityGraph;
import telegramBot.enums.Language;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Getter
    @Setter
    @Column(name = "chat_id")
    private String chatId;

    @Getter
    @Setter
    @Column(name = "active")
    private boolean active;

    public User(String chatId) {
        this.chatId = chatId;
        this.active = true;
    }


    @ManyToMany()
    @JoinTable(name = "Users_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {@JoinColumn(name = "subscription_id")})
    private List<Subscription> subscriptions;

    public void addSubscription(Subscription subscription){
        if(this.subscriptions == null) this.subscriptions = new ArrayList<>();
        this.subscriptions.add(subscription);
        subscription.getUsers().add(this);
    }

    public void removeSubscription(Subscription subscription){
        if(this.subscriptions != null) {
            this.subscriptions.remove(subscription);
            subscription.getUsers().remove(this);
        }

    }
    public List<Subscription> getSubscriptions() {
        if(this.subscriptions == null) this.subscriptions = new ArrayList<>();
        return this.subscriptions;
    }



}
