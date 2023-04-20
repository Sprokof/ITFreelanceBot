package telegramBot.services;

import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.entity.Subscription;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repositories.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;


    @Override
    public synchronized boolean saveIfNotExist(Order order) {
        if (this.orderRepo.exist(order.getLink())) return false;
        this.orderRepo.saveOrder(order);
        pause();
        return true;
    }

    private void pause() {
        try {
            wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void deleteOldOrders() {
        if(orderRepo.containsOldOrders()) {
            this.orderRepo.deleteOldOrders();
            InitStatusService.setInit(false);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                deleteOldOrders();
                waitDay();
            }
        });
    }

    private synchronized void waitDay() {
        try {
            Thread.sleep(86400000);
        } catch (InterruptedException e) {
            e.getCause();

        }
    }

    @Override
    public void saveOrder(Order order) {
        this.orderRepo.saveOrder(order);
    }

    @Override
    public boolean orderExist(Order order) {
        String link = order.getLink();
        return this.orderRepo.exist(link);
    }

    @Override
    public void updateOrder(Order order) {
        this.orderRepo.update(order);
    }

    @Override
    public BigInteger getTaskNum(Order order) {
        String link = order.getLink();
        String digitPat = "\\d+";
        String[] linksItems = link.split("");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < linksItems.length; i++) {
            String item = linksItems[i];
            if (item.matches(digitPat)) {
                int index = i;
                while (linksItems[index].matches(digitPat)) {
                    output.append(linksItems[index]);
                    index++;
                }
            }

        }
        return (new BigInteger(output.toString()));

    }

    @Override
    public BigInteger[] getTasksNums(List<Order> orders) {
       return orders.stream().map(this::getTaskNum).sorted().
               collect(Collectors.toList()).toArray(BigInteger[]::new);
    }



}
