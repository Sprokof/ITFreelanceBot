package telegramBot.repositories;

import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.List;

public interface SubscriptionRepo {
    void addSubscription(User user, Language language);
    void update(Subscription subscription);
    Subscription getSubscriptionByLanguage(Language language);
    boolean exist(Subscription subscription);

    List<Subscription> getSubscriptions();

    boolean subscriptionsExists();
    boolean subscriptionExist(Language language);

    void deleteSubscription(int userId, int subId);
}
