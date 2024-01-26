package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.repository.SubscriptionRepository;
import telegramBot.repository.datajpa.DataJpaSubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    public boolean existByLanguageAndChatId(Language language, String chatId) {
        return this.subscriptionRepository.existByLanguageAndChatId(language, chatId);
    }

    public Subscription getByLanguage(Language language) {
        return this.subscriptionRepository.getByLanguage(language);
    }

    public List<Subscription> getAll() {
        return this.subscriptionRepository.getAll();
    }

    public void update(Subscription subscription) {
        this.subscriptionRepository.save(subscription);
    }

    public boolean exists(long userId) {
        return this.subscriptionRepository.exists(userId);
    }

    public boolean delete(long id, long userId) {
        return this.subscriptionRepository.delete(id, userId);
    }

}
