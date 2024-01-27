package telegramBot.repository.datajpa;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import telegramBot.entity.User;
import telegramBot.repository.UserRepository;

import java.util.List;

@Repository
public class DataJpaUserRepository implements UserRepository {

    private final UserCrudRepository crudRepository;
    public DataJpaUserRepository(UserCrudRepository crudRepository){
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
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
