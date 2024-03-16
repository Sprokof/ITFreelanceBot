package telegramBot.service;

import telegramBot.enums.Language;

import java.util.HashMap;
import java.util.Map;

public class AdminService {

    private final OrderService orderService;
    private final UserService userService;

    public AdminService(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

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
