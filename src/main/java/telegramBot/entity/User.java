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
@Table(name = "USERS")
@NoArgsConstructor
public class User {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @Column(name = "CHAT_ID")
    private String chatId;

    @Getter
    @Setter
    @Column(name = "active")
    private boolean active;

    public User(String chatId) {
        this.chatId = chatId;
        this.active = true;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof User)) return false;
        User user = (User) obj;
        return this.id == user.id;
    }

    @Setter
    @ManyToMany(mappedBy = "users")
    @LazyCollection(LazyCollectionOption.FALSE)
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
