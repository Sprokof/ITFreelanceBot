package telegramBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.ArrayList;
import java.util.List;
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
    public synchronized void executeNotices() {
        if(!InitStatusService.init()) return ;
            List<Order> orders = this.exchangeService.findNewOrders();
            if(orders.isEmpty()) return ;
            List<User> activeUsers = this.userService.getActiveUsers();
            activeUsers.forEach(user -> {
            List<OrderDto> filteredOrders = orders.stream().filter(o -> {
                    Subscription orderSubscription = o.getSubscription();
                    return user.getSubscriptions().contains(orderSubscription);
                }).map(OrderDto :: toDto).collect(Collectors.toList());
                if(!filteredOrders.isEmpty()) this.messageService.
                        sendNotice(user.getChatId(), filteredOrders);
            });
            orders.forEach(order -> orderService.saveOrder(order));

    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while(true){
                executeNotices();
                pause();
            }
        }).start();
    }

    private void pause() {
        try {
            Thread.sleep(420000);
        }
        catch (InterruptedException e){
            e.getCause();
        }

    }


}
