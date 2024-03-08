package telegramBot.repository;

import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User getByChatId(String chatId);
    List<User> getAllActive();
    int countByStatus(boolean active);
    int countSubscribed(Language language);


}
