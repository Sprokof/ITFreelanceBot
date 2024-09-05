package telegramBot.service;

import org.springframework.stereotype.Service;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.enums.Language;
import telegramBot.enums.SubscriptionStatus;
import telegramBot.repository.ExchangeRepository;
import telegramBot.parser.job.ExchangesOrdersJob;
import telegramBot.util.OrderUtil;

import java.util.*;

@Service
public final class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    private final SubscriptionService subscriptionService;

    private final OrderService orderService;

    private final ExchangesOrdersJob parser;

    public ExchangeService(ExchangeRepository exchangeRepository, SubscriptionService subscriptionService,
                           OrderService orderService, ExchangesOrdersJob parser) {
        this.exchangeRepository = exchangeRepository;
        this.subscriptionService = subscriptionService;
        this.orderService = orderService;
        this.parser = parser;
    }

    public Exchange get(telegramBot.enums.Exchange exchange) {
        return this.exchangeRepository.getByName(exchange.getName());
    }

    // method for firstly fill db
    public void fill() {
        List<Subscription> subscriptions = subscriptionService.getAllByStatus(SubscriptionStatus.CREATE);
        telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.get();
        for (Subscription subscription : subscriptions) {
            Language language = Language.ignoreCaseValueOf(subscription.getLanguage());
            int id = subscriptionService.getIdByLanguage(language);
            Map<telegramBot.enums.Exchange, List<Order>> exchangesOrders = this.parser.findOrders(language).waitFinish().get();
            for (telegramBot.enums.Exchange e : exchanges) {
                List<Order> orders = exchangesOrders.get(e);
                telegramBot.entity.Exchange exchange = get(e);
                for (Order order : orders) {
                    if (orderService.saveIfNotExist(order, id)) {
                        order.setExchange(exchange);
                        order.setSubscription(subscription);
                        this.orderService.update(order);
                    }
                }
            }
            subscription.setStatus(SubscriptionStatus.INIT);
            subscriptionService.update(subscription);
        }
    }


    public void update(Exchange exchange) {
        this.exchangeRepository.save(exchange);
    }


    public Set<OrderDto> findNewOrders(Language language) {
        Set<OrderDto> newOrders = new HashSet<>();
        telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.get();
        Map<telegramBot.enums.Exchange, List<Order>> taskOrders = this.parser.findOrders(language).waitFinish().get();
        for (telegramBot.enums.Exchange e : exchanges) {
            List<Order> ordersByExchange = taskOrders.get(e);
            for (Order order : ordersByExchange) {
                int id = subscriptionService.getIdByLanguage(language);
                if (!this.orderService.exist(order.getLink(), id)) {
                    Exchange exchange = this.exchangeRepository.getByName(e.getName());
                    Subscription subscription = this.subscriptionService.getByLanguage(language);

                    order.setExchange(exchange);
                    order.setSubscription(subscription);
                    newOrders.add(OrderUtil.toDto(order));

                }
            }
        }
        return newOrders;
    }
}
