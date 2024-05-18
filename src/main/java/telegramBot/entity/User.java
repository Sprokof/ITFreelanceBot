package telegramBot.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import telegramBot.enums.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Getter
    @Setter
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String chatId, Role role) {
        this.chatId = chatId;
        this.role = role;
        this.active = true;
    }


    @ManyToMany
    @JoinTable(name = "Users_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {@JoinColumn(name = "subscription_id")})
    private Set<Subscription> subscriptions;

    public void addSubscription(Subscription subscription){
        if(this.subscriptions == null) this.subscriptions = new HashSet<>();
        this.subscriptions.add(subscription);
    }

    public void removeSubscription(Subscription subscription){
        if(this.subscriptions != null) {
            this.subscriptions.remove(subscription);
        }

    }
    public Set<Subscription> getSubscriptions() {
        if (this.subscriptions == null) {
            this.subscriptions = new HashSet<>();
        }
        return this.subscriptions;
    }

    public boolean subscriptionExist(Subscription subscription) {
        return this.subscriptions.contains(subscription);
    }

}
