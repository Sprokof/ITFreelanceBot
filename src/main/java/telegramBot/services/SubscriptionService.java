package telegramBot.services;

import telegramBot.entity.Subscription;
import telegramBot.enums.Language;

import java.util.List;

public interface SubscriptionService {
    Subscription getSubscriptionByLanguage(Language language);
    boolean exist(Language language);
    List<Subscription> getSubscriptions();
    void update(Subscription subscription);
    boolean subscriptionsExists();
    void deleteSubscription(int userId, int subId);


}
