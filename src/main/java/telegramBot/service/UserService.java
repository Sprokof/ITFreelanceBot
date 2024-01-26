package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.repository.UserRepository;
import telegramBot.repository.datajpa.DataJpaUserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User getByChatId(String chatId) {
        return this.userRepository.getByChatId(chatId);
    }

    public void addSubscription(User user, Subscription subscription) {
        if(!user.isActive()) user.setActive(true);
        user.addSubscription(subscription);
        this.update(user);
    }

    public void update(User user){
        this.userRepository.save(user);
    }

    public User createOrGet(String chatId) {
        User user = getByChatId(chatId);
        if(user == null) return save(new User(chatId));
        return user;

    }


    public void removeSubscription(User user, Subscription subscription) {
        user.removeSubscription(subscription);
        if(user.getSubscriptions().isEmpty()) user.setActive(false);
        this.update(user);
    }

    public void setActive(String chatId, boolean flag) {
        User user = getByChatId(chatId);
        user.setActive(flag);
        update(user);
    }


    public List<User> getAllActive() {
        return this.userRepository.getAllActive();
    }
}
