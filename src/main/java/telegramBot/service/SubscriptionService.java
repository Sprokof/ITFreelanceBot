package telegramBot.service;

import org.springframework.stereotype.Service;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.repository.SubscriptionRepository;
import java.util.List;

@Service
public final class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription getByLanguage(Language language) {
        return this.subscriptionRepository.getByLanguage(language);
    }

    public List<Subscription> getAllByStatus(SubscriptionStatus status) {
        return this.subscriptionRepository.getAllByStatus(status);
    }

    public void update(Subscription subscription) {
        this.subscriptionRepository.save(subscription);
    }

    public int getIdByLanguage(Language language) {
        return this.subscriptionRepository.getIdByLanguage(language);
    }

    public List<Subscription> getAllByUserChatId(String chatId) {
        return this.subscriptionRepository.getAllByUserChatId(chatId);
    }

}
