package telegramBot.service;

import org.springframework.boot.CommandLineRunner;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Order;
import telegramBot.enums.Exchange;
import telegramBot.enums.Language;
import telegramBot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.task.Task;
import telegramBot.util.BotUtil;

import java.time.LocalDate;
import java.util.*;

@Component
public class OrderService implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private Task task;


    public boolean saveIfNotExist(Order order) {
        if (this.orderRepository.exist(order.getLink())) return false;
        this.orderRepository.save(order);
        return true;
    }


    public synchronized void deleteOld() {
        for(Exchange exchange : Exchange.getExchanges()) {
            LocalDate deleteDate = currentDateMinusExchangeRefreshInterval(exchange.getRefreshInterval());
            this.orderRepository.deleteByExchangeAndDate(exchange, deleteDate);
        }

        waitDay();
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            while (true) {
                deleteOld();
            }
        }).start();
    }

    private synchronized void waitDay() {
        try {
            Thread.sleep(BotUtil.DAY_MILLISECONDS);
        } catch (InterruptedException e) {
            e.getCause();

        }
    }

    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    public boolean exist(Order order) {
        String link = order.getLink();
        return this.orderRepository.exist(link);
    }

    public void update(Order order) {
        this.orderRepository.save(order);
    }
    

    public String getLatestOrdersMessage(Update update) {
        String input = update.getMessage().getText().
                replaceAll("\\s", "");
        String[] items = extractItems(input);
        Language language = Language.getLanguageByValue(items[0]);
        int count = Integer.parseInt(items[1]);
        List<Order> orders = this.orderRepository.getAllByLanguage(language);
        List<OrderDto> dtos = OrderDto.toDtos(orders).subList(0,
                getEndIndex(orders.size(), count));
        assert language != null;
        return createMessage(language, dtos);
    }

    private String createMessage(Language language, List<OrderDto> dtos){
        StringBuilder sb = new StringBuilder("Последние заказы по запросу " +
                language.getName() + ":\n" );
        dtos.forEach(o -> {
            sb.append(o.getOrderInfo()).append(" (").
                    append(o.getExchangeName()).
                    append(")").append("\n\n");
        });

    return sb.toString();
    }

    private int getEndIndex(int collectionSize, int count){
        return Math.min(collectionSize, count);
    }

    private String[] extractItems(String input){
        String[] items = input.split(",");
        String language = items[0];
        String count = (items[1].
                matches("\\d+") &&
                !items[1].equals("0")) ? items[1] : "1" ;
        return new String[]{language, count};
    }

    private LocalDate currentDateMinusExchangeRefreshInterval(int interval){
        return LocalDate.now().minusDays(interval);
    }

}
