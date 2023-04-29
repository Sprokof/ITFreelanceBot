package telegramBot.services;

import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.repositories.UserRepo;
import telegramBot.repositories.UserRepoImpl;
import telegramBot.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo = new UserRepoImpl();

    private final SubscriptionService subscriptionService = new SubscriptionServiceImpl();


    @Override
    public void saveUser(User user) {
        if(!this.userRepo.exist(user)) {
            this.userRepo.saveUser(user);
        }

    }

    @Override
    public User getUserByChatId(String chatId) {
        return this.userRepo.getUserByChatId(chatId);
    }

    @Override
    public void addSubscription(User user, Update update) {
        String input = update.getMessage().getText();
        Language language = Language.getLanguageByValue(input);
        Subscription subscription = this.subscriptionService.
                getSubscriptionByLanguage(language);
        if(!user.isActive()) user.setActive(true);
        user.addSubscription(subscription);
        this.subscriptionService.update(subscription);
    }

    @Override
    public void update(User user){
        this.userRepo.update(user);
    }

    @Override
    public User createUser(String chatId) {
        if(exist(chatId)) {
            return getUserByChatId(chatId);
        }
        User user = new User(chatId);
        saveUser(user);
        return user;
    }

    @Override
    public boolean exist(String chatId) {
        return this.userRepo.exist(new User(chatId));
    }

    @Override
    public List<User> getAllActiveUsers() {
        return this.userRepo.getAllActiveUsers();
    }

    @Override
    public void removeSubscription(User user, Update update) {
        String language = update.getMessage().getText();
        Subscription subscription = this.subscriptionService.
                getSubscriptionByLanguage(Language.getLanguageByValue(language));
        user.removeSubscription(subscription);
        if(user.getSubscriptions().isEmpty()) user.setActive(false);
        this.subscriptionService.update(subscription);
    }

    @Override
    public void setActive(String chatId, boolean flag) {
        User user = getUserByChatId(chatId);
        user.setActive(flag);
        update(user);
    }
}
