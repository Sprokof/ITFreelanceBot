package telegramBot.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import telegramBot.util.BotUtil;

import java.time.LocalDate;
import java.util.*;

@Service
public final class OrderService implements CommandLineRunner {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public boolean saveIfNotExist(Order order, int id) {
        if (this.orderRepository.existByLink(order.getLink(), id)) {
            return false;
        }
        this.orderRepository.save(order);
        return true;
    }


    public void deleteOld() {
        for(Exchange exchange : Exchange.get()) {
            LocalDate deleteDate = currentDateMinusExchangeRefreshInterval(exchange.getRefreshInterval());
            this.orderRepository.deleteByExchangeAndDate(exchange, deleteDate);
        }
        waitDay();
    }

    @Override
    public void run(String... args) throws Exception {
        deleteOld();
    }

    public void waitDay() {
        try {
            Thread.sleep(BotUtil.DAY_MILLISECONDS);
        } catch (InterruptedException e) {
            e.getCause();
        }
    }

    public Order create(Order order) {
        return this.orderRepository.save(order);
    }

    public boolean exist(String link, int subscriptionId) {
        return this.orderRepository.existByLink(link, subscriptionId);
    }

    public List<Order> getAllByLanguage(Language language){
        return this.orderRepository.getAllByLanguage(language);
    }

    public void update(Order order) {
        this.orderRepository.save(order);
    }
    
    private LocalDate currentDateMinusExchangeRefreshInterval(int interval){
        return LocalDate.now().minusDays(interval);
    }

    public int getOrdersCount(Language language) {
        return this.orderRepository.count(language);
    }
}
