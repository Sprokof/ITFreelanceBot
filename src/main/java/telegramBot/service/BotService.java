package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.util.BotUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BotService implements CommandLineRunner {

    @Autowired
    private @Lazy ExchangeService exchangeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            Set<OrderDto> all = new HashSet<>();
            while (true) {
                for (Subscription subscription : this.subscriptionService.getAllByStatus(SubscriptionStatus.INIT)) {
                    Language language = Language.ignoreCaseValueOf(subscription.getLanguage());
                    all.addAll(this.exchangeService.findNewOrders(language));
                }
                    all.forEach(dto -> orderService.create(dto.toEntity(true)));
                    executeNotices(getFilteredOrderDtos(all));
                    all.clear();
                pause();
            }
        }).start();

    }

    public void executeNotices(Map<String, Set<OrderDto>> orders) {
        for (Map.Entry<String, Set<OrderDto>> entry : orders.entrySet()) {
            this.messageService.sendNotice(entry.getKey(), entry.getValue());
        }
    }

    private Map<String, Set<OrderDto>> getFilteredOrderDtos(Set<OrderDto> newDtos){
        Map<String, Set<OrderDto>> orders = new HashMap<>();
        if(newDtos.isEmpty()) return new HashMap<>();
        this.userService.getAllActive().forEach(user -> {
            Set<OrderDto> filteredOrders = newDtos.stream()
                    .filter(dto -> user.subscriptionExist(dto.getSubscription()))
                    .collect(Collectors.toSet());
            if (!filteredOrders.isEmpty()) {
                orders.put(user.getChatId(), filteredOrders);
            }
        });
    return orders;
    }

    private void pause() {
        try {
            Thread.sleep(BotUtil.EXECUTE_NOTICE_TIMEOUT);
        } catch (InterruptedException e) {
            e.getCause();
        }
    }


}
