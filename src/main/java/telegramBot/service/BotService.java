package telegramBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.BotStatus;
import telegramBot.repository.BotStatusRepository;
import telegramBot.util.BotUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class BotService implements CommandLineRunner {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BotStatusRepository botStatusRepository;


    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                if (status() == BotStatus.INIT) {
                    List<Order> newOrders = this.exchangeService.findNewOrders();
                    newOrders.forEach(order -> this.orderService.save(order));
                    Map<String, List<OrderDto>> orders = getFilteredOrders(newOrders);
                    executeNotices(orders);
                }
                pause();
            }
        }).start();

    }

    public void executeNotices(Map<String, List<OrderDto>> orders) {
        for(User user : this.userService.getAllActive()){
            List<OrderDto> usersOrders = orders.get(user.getChatId());
            if(usersOrders != null) this.messageService.
                    sendNotice(user.getChatId(), usersOrders);
        }
    }

    private Map<String, List<OrderDto>> getFilteredOrders(List<Order> newOrders){
        Map<String, List<OrderDto>> orders = new HashMap<>();
        if(newOrders.isEmpty()) return new HashMap<>();
        List<User> users = this.userService.getAllActive();
        users.forEach(user -> {
            List<OrderDto> filteredOrders = newOrders.stream().filter(order -> {
                Subscription sub = order.getSubscription();
                return user.getSubscriptions().contains(sub);
            }).filter(distinctByKey(Order :: getLink)).
                    map(OrderDto :: toDto).
                    collect(Collectors.toList());
            if(!filteredOrders.isEmpty()) orders.put(user.getChatId(), filteredOrders);
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

    public BotStatus status() {
        String status = this.botStatusRepository.getStatus();
        return BotStatus.valueOf(status);
    }

    public void setBotStatus(BotStatus status) {
        this.botStatusRepository.setStatus(status);
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
