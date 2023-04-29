package telegramBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import telegramBot.dto.OrderDto;
import telegramBot.entity.Subscription;
import telegramBot.entity.User;
import telegramBot.enums.Language;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    public synchronized void executeNotices() {
        if(!InitStatusService.init() || !this.subscriptionService.subscriptionsExists()) return ;
        for(User user : this.userService.getAllActiveUsers()){
            List<Subscription> subscriptions = user.getSubscriptions();
            List<OrderDto> dtos = new ArrayList<>();
            for(Subscription subscription : subscriptions){
                Language subLanguage = Language.getLanguageByValue(subscription.getLanguage());
                addNewOrderIfExist(dtos, this.exchangeService.findNewOrdersByLanguage(subLanguage));
            }
            if(!dtos.isEmpty()) this.messageService.sendNotice(user.getChatId(), dtos);

        }
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

    private void addNewOrderIfExist(List<OrderDto> target, List<OrderDto> newOrders){
        if(!newOrders.isEmpty()) target.addAll(newOrders);

    }

}
