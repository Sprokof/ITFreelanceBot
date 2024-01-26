package telegramBot.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import telegramBot.entity.User;
import org.springframework.stereotype.Component;
import telegramBot.repository.UserRepository;

import java.util.List;

@Component
public class DataJpaUserRepository implements UserRepository {

    @Autowired
    private UserCrudRepository crudRepository;

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public User getByChatId(String chatId) {
        return this.crudRepository.getByChatId(chatId);
    }
    @Override
    public List<User> getAllActive() {
        return this.crudRepository.getAllActive();
    }
}
