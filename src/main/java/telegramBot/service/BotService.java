package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.service.runner.ExchangeRunner;
import telegramBot.util.BotUtil;
import telegramBot.util.ListUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class BotService implements CommandLineRunner {

    private final @Lazy ExchangeRunner exchangeRunner;

    private final MessageService messageService;

    private final UserService userService;

    private final OrderService orderService;

    private final SubscriptionService subscriptionService;

    public BotService(ExchangeRunner exchangeRunner, MessageService messageService, UserService userService,
                      OrderService orderService, SubscriptionService subscriptionService) {
        this.exchangeRunner = exchangeRunner;
        this.messageService = messageService;
        this.userService = userService;
        this.orderService = orderService;
        this.subscriptionService = subscriptionService;
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(BotUtil.SIZE);

    @Override
    public void run(String... args) throws Exception {
        Set<OrderDto> all = new HashSet<>();
        while (true) {
            for (Subscription subscription : this.subscriptionService.getAllByStatus(SubscriptionStatus.INIT)) {
                Language language = Language.ignoreCaseValueOf(subscription.getLanguage());
                all.addAll(this.exchangeRunner.findNewOrders(language));
            }
            all.forEach(dto -> orderService.create(dto.toEntity(true)));
            List<User> activeUsers = this.userService.getAllActive();
            if (activeUsers.size() > BotUtil.SIZE) {
                List<List<User>> usersPartition = ListUtil.partition(activeUsers);
                for (List<User> users : usersPartition) {
                    executorService.execute(() -> filterAndExecute(users, all));
                }
                executorService.shutdown();
                while (!executorService.isShutdown()) {
                    wait();
                }
            } else {
                filterAndExecute(activeUsers, all);
            }
            all.clear();
            pause();
        }
    }

    private void filterAndExecute(List<User> activeUsers, Set<OrderDto> newDtos) {
        if (newDtos.isEmpty()) return ;
        activeUsers.forEach(user -> {
            Set<OrderDto> filteredOrders = newDtos.stream()
                    .filter(dto -> user.subscriptionExist(dto.getSubscription()))
                    .collect(Collectors.toSet());
            if (!filteredOrders.isEmpty()) {
                this.messageService.sendNotice(user.getChatId(), filteredOrders);
            }
        });
    }

    private void pause() {
        try {
            Thread.sleep(BotUtil.EXECUTE_NOTICE_TIMEOUT);
        } catch (InterruptedException e) {
            e.getCause();
        }
    }

}
