package telegramBot.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import telegramBot.entity.Exchange;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.enums.BotStatus;
import telegramBot.enums.Language;
import telegramBot.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.task.ExchangeParser;

import java.util.*;

@Service
public class ExchangeService implements CommandLineRunner {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private @Lazy BotService botService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExchangeParser parser;

    public Exchange get(telegramBot.enums.Exchange exchange) {
        return this.exchangeRepository.getByName(exchange.getName());
    }

    public void init() {
        if(botService.status() == BotStatus.CREATE) {
            Language[] languages = Language.getLanguages();
            telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.getExchanges();
            for (Language language : languages) {
                Subscription subscription = this.subscriptionService.getByLanguage(language);
                Map<telegramBot.enums.Exchange, List<Order>> exchangesOrders = this.parser.getOrders(language);
                for (telegramBot.enums.Exchange e : exchanges) {
                    List<Order> orders = exchangesOrders.get(e);
                    telegramBot.entity.Exchange exchange = get(e);
                    for (Order order : orders) {
                        if (orderService.saveIfNotExist(order)) {
                            order.setExchange(exchange);
                            order.setSubscription(subscription);
                            this.orderService.update(order);
                        }
                    }
                }
            }
            botService.setBotStatus(BotStatus.INIT);
        }

    }


    public void update(Exchange exchange) {
        this.exchangeRepository.save(exchange);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(this::init).start();
    }

    public List<Order> findNewOrders() {
        List<Order> newOrders = new ArrayList<>();
        for(Language language : Language.getLanguages()) {
            telegramBot.enums.Exchange[] exchanges = telegramBot.enums.Exchange.getExchanges();
            Map<telegramBot.enums.Exchange, List<Order>> taskOrders = this.parser.getOrders(language);
            for (telegramBot.enums.Exchange e : exchanges) {
                List<Order> ordersByExchange = taskOrders.get(e);
                for (Order order : ordersByExchange) {
                    if (!this.orderService.exist(order)) {
                        Exchange exchange = this.exchangeRepository.getByName(e.getName());
                        Subscription subscription = this.subscriptionService.getByLanguage(language);

                        order.setExchange(exchange);
                        order.setSubscription(subscription);
                        newOrders.add(order);

                    }
                }
            }
        }

        return newOrders;

    }





}
