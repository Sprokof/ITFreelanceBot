package telegramBot.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
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
    }

    public void removeSubscription(Subscription subscription){
        if(this.subscriptions != null) {
            this.subscriptions.remove(subscription);
        }

    }
    public List<Subscription> getSubscriptions() {
        if(this.subscriptions == null) this.subscriptions = new ArrayList<>();
        return this.subscriptions;
    }



}
