package telegramBot.repository;

import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Subscription getByLanguage(Language language);
    List<Subscription> getAllByStatus(SubscriptionStatus status);
    int getIdByLanguage(Language language);


}
