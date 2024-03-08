package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    public int subscriptionsCount() {
        return Language.getLanguages().length;
    }
    public Map<String, Integer> subscriptionOrdersCount() {
        Map<String, Integer> subscriptionOrdersCount = new HashMap<>();
        for (Language language : Language.getLanguages()) {
            int count = orderService.getOrdersCount(language);
            subscriptionOrdersCount.put(language.getName(), count);
        }
    return subscriptionOrdersCount;
    }

    public Map<String, Integer> subscribedUsersCount(){
        Map<String, Integer> subscriptionUsersCount = new HashMap<>();
        for (Language language : Language.getLanguages()) {
            int count = userService.countSubscribed(language);
            subscriptionUsersCount.put(language.getName(), count);
        }
        return subscriptionUsersCount;
    }

    public int activeUsersCount() {
        return userService.countByStatus(true);
    }

    public int nonActiveUsersCount() {
        return userService.countByStatus(false);
    }
}
