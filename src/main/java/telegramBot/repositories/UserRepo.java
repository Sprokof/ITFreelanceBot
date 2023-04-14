package telegramBot.repositories;

import telegramBot.entity.User;

import java.util.List;

public interface UserRepo {
    void saveUser(User user);
    User getUserByChatId(String chatId);
    void update(User user);
    boolean exist(User user);
    List<User> getAllUsers();


}
