package telegramBot.services;

import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.repositories.SubscriptionRepo;
import telegramBot.repositories.SubscriptionRepoImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {

    public static final String ADD_SUCCESS = "Subscription successfully added";
    public static final String REMOVE_SUCCESS = "Subscription successfully removed";

    private final SubscriptionRepo subscriptionRepo = new SubscriptionRepoImpl();


    @Override
    public boolean exist(Language language) {
        return this.subscriptionRepo.exist(new Subscription(language));
    }

    @Override
    public Subscription getSubscriptionByLanguage(Language language) {
        return this.subscriptionRepo.getSubscriptionByLanguage(language);
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return this.subscriptionRepo.getSubscriptions();
    }

    @Override
    public void update(Subscription subscription) {
        this.subscriptionRepo.update(subscription);
    }

    @Override
    public boolean subscriptionsExists() {
        return this.subscriptionRepo.subscriptionsExists();
    }

    @Override
    public void deleteSubscription(int userId, int subId) {
        this.subscriptionRepo.deleteSubscription(userId, subId);
    }

}
