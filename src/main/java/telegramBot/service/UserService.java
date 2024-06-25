package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegramBot.enums.Language;
import telegramBot.enums.Role;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User getByChatId(String chatId) {
        return this.userRepository.getByChatId(chatId);
    }

    public void addSubscription(User user, Subscription subscription) {
        if (!user.isActive()) {
            user.setActive(true);
        }
        user.addSubscription(subscription);
        this.update(user);
    }
    public void update(User user){
        this.userRepository.save(user);
    }

    public User createOrGet(String chatId) {
        User user = getByChatId(chatId);
        if(user == null) {
            return save(new User(chatId, (Role.isAdmin(chatId) ? Role.ADMIN : Role.USER)));
        }
        return user;
    }
    public void removeSubscription(User user, Subscription subscription) {
        user.removeSubscription(subscription);
        if(user.getSubscriptions().isEmpty()) {
            user.setActive(false);
        }
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

    public int countByStatus(boolean active){
        return this.userRepository.countByStatus(active);
    }
    public int countSubscribed(Language language) {
        return this.userRepository.countSubscribed(language);
    }

}
