package telegramBot.services;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.repositories.ExchangeRepo;
import telegramBot.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepo exchangeRepo;

    @Autowired
    private SubscriptionService subscriptionService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private Task task;

    @Override
    public Exchange getExchange(telegramBot.enums.Exchange exchange) {
        return this.exchangeRepo.getExchangeByName(exchange.getName());
    }

    @Override
    public void init() {
        if(!initCondition()) return ;
        Language[] languages = Language.getLanguages();
        telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.getExchanges();
        for(Language language : languages) {
            Subscription subscription = this.subscriptionService.getSubscriptionByLanguage(language);
            Map<telegramBot.enums.Exchange, List<Order>> exchangesOrders = this.task.getOrders(language);
            for (telegramBot.enums.Exchange e : exchanges) {
                List<Order> orders = exchangesOrders.get(e);
                telegramBot.entity.Exchange exchange = getExchange(e);
                for (Order order : orders) {
                    if (this.orderService.saveIfNotExist(order)) {
                        exchange.addOrder(order);
                        subscription.addOrder(order);

                        this.orderService.updateOrder(order);
                    }
                }
            }

        }
        InitStatusService.setInit();

    }


    @Override
    public void update(Exchange exchange) {
        this.exchangeRepo.update(exchange);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(this::init).start();
    }

    private boolean initCondition() {
        return !InitStatusService.init();
    }

    @Override
    public List<OrderDto> findNewOrdersByLanguage(Language language) {
        List<Order> newOrders = new ArrayList<>();
        telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.getExchanges();
        Map<telegramBot.enums.Exchange, List<Order>> taskOrders = this.task.getOrders(language);
        for (telegramBot.enums.Exchange e : exchanges) {
            List<Order> ordersByExchange = taskOrders.get(e);
            for (Order order : ordersByExchange) {
                if (!this.orderService.orderExist(order)) {
                    Exchange exchange = this.exchangeRepo.getExchangeByName(e.getName());
                    Subscription subscription = this.subscriptionService.
                            getSubscriptionByLanguage(language);

                    order.setExchange(exchange);
                    order.setSubscription(subscription);
                    newOrders.add(order);

                }
            }
        }

        newOrders.forEach(order -> this.orderService.saveOrder(order));
        return OrderDto.toDtos(newOrders);

    }



}
