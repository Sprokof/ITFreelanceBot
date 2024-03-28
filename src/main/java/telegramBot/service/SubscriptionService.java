package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.repository.SubscriptionRepository;
import telegramBot.repository.datajpa.DataJpaSubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

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

}
