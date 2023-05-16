package telegramBot.services;

import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.enums.Language;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    User getUserByChatId(String chatId);

    void addSubscription(User user, Update update);
    void update(User user);

    User createUser(String chatId);

    boolean exist(String chatId);
    List<User> getAllActiveUsersWithSubscription(Subscription subscription);
    List<User> getActiveUsers();

    void removeSubscription(User user, Update update);

    void setActive(String chatId, boolean flag);
}
