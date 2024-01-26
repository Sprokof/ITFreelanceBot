package telegramBot.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import org.springframework.stereotype.Component;
import telegramBot.repository.SubscriptionRepository;

import java.util.List;


@Component
public class DataJpaSubscriptionRepository implements SubscriptionRepository {

    @Autowired
    private SubscriptionCrudRepository crudRepository;

    @Override
    public Subscription save(Subscription subscription) {
        return this.crudRepository.save(subscription);
    }

    @Override
    public Subscription getByLanguage(Language language) {
        return this.crudRepository.getByLanguage(language.getName());
    }

    @Override
    public boolean existByLanguageAndChatId(Language language, String chatId) {
        return this.crudRepository.existByLanguageAndChatId(language.getName(), chatId) != 0;
    }

    @Override
    public List<Subscription> getAll() {
        return (List<Subscription>) this.crudRepository.findAll();
    }

    @Override
    public boolean exists(long userId) {
        return this.crudRepository.exists(userId) != 0;
    }

    @Override
    public boolean delete(long id, long userId) {
        return this.crudRepository.delete(id, userId) != 0;
    }
}
