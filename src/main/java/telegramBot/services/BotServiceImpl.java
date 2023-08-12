package telegramBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.BotStatus;
import telegramBot.enums.Language;
import telegramBot.repositories.BotStatusRepo;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BotServiceImpl implements BotService {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BotStatusRepo botStatusRepo;


    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                if (InitStatusService.init()) {
                    List<Order> newOrders = this.exchangeService.findNewOrders();
                    newOrders.forEach(order -> this.orderService.saveOrder(order));
                    Map<String, Set<OrderDto>> orders = getFilteredOrders(newOrders);
                    executeNotices(orders);
                }
                pause();
            }
        }).start();

    }

    @Override
    public void executeNotices(Map<String, Set<OrderDto>> orders) {
        for(User user : this.userService.getActiveUsers()){
            Set<OrderDto> usersOrders = orders.get(user.getChatId());
            if(usersOrders != null) this.messageService.
                    sendNotice(user.getChatId(), usersOrders);
        }
    }

    private Map<String, Set<OrderDto>> getFilteredOrders(List<Order> newOrders){
        Map<String, Set<OrderDto>> orders = new HashMap<>();
        if(newOrders.isEmpty()) return new HashMap<>();
        List<User> users = this.userService.getActiveUsers();
        users.forEach(user -> {
            Set<OrderDto> filteredOrders = newOrders.stream().filter(order -> {
                Subscription sub = order.getSubscription();
                return user.getSubscriptions().contains(sub);
            }).map(OrderDto::toDto).collect(Collectors.toSet());
            if(!filteredOrders.isEmpty()) orders.put(user.getChatId(), filteredOrders);
        });

    return orders;
    }

    private void pause() {
        try {
            Thread.sleep(420000);
        } catch (InterruptedException e) {
            e.getCause();
        }
    }

    @Override
    public BotStatus status() {
        String status = this.botStatusRepo.getStatus();
        return BotStatus.statusValueOf(status);
    }

    @Override
    public void setBotStatus(BotStatus status) {
        this.botStatusRepo.setStatus(status.getStatus());
    }


}
