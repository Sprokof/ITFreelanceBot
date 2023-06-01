package telegramBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                if (!InitStatusService.init()) return;
                List<Order> newOrders = this.exchangeService.findNewOrders();
                if (newOrders.isEmpty()) return;
                Map<User, List<OrderDto>> orders = getFilteredOrders(newOrders);
                executeNotices(orders);
                pause();
            }
        });

    }

    @Override
    public void executeNotices(Map<User, List<OrderDto>> orders) {
        if(orders.isEmpty()) return;
        for(User user : this.userService.getActiveUsers()){
            List<OrderDto> userOrders = orders.get(user);
            this.messageService.sendNotice(user.getChatId(), userOrders);
        }
    }

    private Map<User, List<OrderDto>> getFilteredOrders(List<Order> newOrders){
        Map<User, List<OrderDto>> orders = new HashMap<>();
        if(newOrders.isEmpty()) return orders;
        List<User> users = this.userService.getActiveUsers();
        users.forEach(user -> {
            List<OrderDto> filteredOrders = newOrders.stream().filter(order -> {
                Subscription sub = order.getSubscription();
                return user.getSubscriptions().contains(sub);
            }).map(OrderDto::toDto).collect(Collectors.toList());
        orders.put(user, filteredOrders);
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


}
