package telegramBot.repository;

import telegramBot.entity.User;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User getByChatId(String chatId);
    List<User> getAllActive();


}
