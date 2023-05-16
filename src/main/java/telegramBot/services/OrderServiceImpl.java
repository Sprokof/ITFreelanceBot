package telegramBot.services;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repositories.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.tasks.Task;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private Task task;


    @Override
    public boolean saveIfNotExist(Order order) {
        if (this.orderRepo.exist(order.getLink())) return false;
        this.orderRepo.saveOrder(order);
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
    public synchronized void deleteOldOrders() {
        if(orderRepo.containsOldOrders()) {
            this.orderRepo.deleteOldOrders();
        }
        waitDay();
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                deleteOldOrders();
            }
        }).start();
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


    @Override
    public String getLatestOrdersMessage(Update update) {
        String input = update.getMessage().getText();
        Language language = Language.getLanguageByValue(input);
        List<OrderDto> dtos = OrderDto.toDtos(this.orderRepo.
                getOrdersByLanguage(language)).subList(0,7);
        assert language != null;
        return createMessage(language, dtos);
    }

    private String createMessage(Language language, List<OrderDto> dtos){
        StringBuilder sb = new StringBuilder("Последние заказы по запросу " + language.getName() + ":\n" );
        dtos.forEach(o -> {
            sb.append(o.getOrderInfo()).append("\n").
                    append(MessageServiceImpl.delim());
        });
    return sb.toString();
    }
}
